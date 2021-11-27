package robotuprising.ftc2021.subsystems

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.*
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMUImpl
import com.qualcomm.robotcore.hardware.VoltageSensor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import robotuprising.ftc2021.auto.drive.DriveConstants
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequence
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceBuilder
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceRunner
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.hardware.AxesSigns
import robotuprising.lib.hardware.BNO055IMUUtil.remapAxes
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.system.statemachine.StateMachineBuilder
import robotuprising.lib.util.Extensions.d
import java.util.*
import kotlin.math.absoluteValue

class Ayame : MecanumDrive(DriveConstants.kV, DriveConstants.kA, DriveConstants.kStatic, DriveConstants.TRACK_WIDTH), Subsystem {

    // fields
    // drive motors
    private val fl = NakiriMotor("FL", true).brake.openLoopControl.reverse
    private val bl = NakiriMotor("BL", true).brake.openLoopControl.reverse
    private val fr = NakiriMotor("FR", true).brake.openLoopControl
    private val br = NakiriMotor("BR", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

    private val ultrasonics = Ultrasonics()

    // imu
    private val imu = BulkDataManager.hwMap[BNO055IMUImpl::class.java, "imu"]
    private val headingOffset: Double
    private val imuOffsetRead: Double get() = imu.angularOrientation.firstAngle - headingOffset

    private val thirdHeadingOffset: Double

    val imuThirdAngle: Double get() = imu.angularOrientation.thirdAngle - thirdHeadingOffset

    // powers
    private var wheels: List<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0)

    // roadrunner fields
    private val trajectorySequenceRunner: TrajectorySequenceRunner
    private val follower: TrajectoryFollower
    private val batteryVoltageSensor: VoltageSensor

    private var following = false
    private var heading = Angle(0.0, AngleUnit.RAD)

    private var TRANSLATIONAL_PID = PIDCoefficients(6.0, 0.0, 0.5)
    private var HEADING_PID = PIDCoefficients(5.0, 0.0, 0.5)

    private val VEL_CONSTRAINT: TrajectoryVelocityConstraint = getVelocityConstraint(DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH)
    private val ACCEL_CONSTRAINT: TrajectoryAccelerationConstraint = getAccelerationConstraint(DriveConstants.MAX_ACCEL)

    fun getBulkDataTicks(): List<Double> {
        return motors.map { it.bulkPosition.d }
    }

    fun setVectorPower(powers: Pose) {
        wheels = mutableListOf(
            powers.y + powers.x + powers.h.angle,
            powers.y - powers.x + powers.h.angle,
            powers.y - powers.x - powers.h.angle,
            powers.y + powers.x - powers.h.angle
        )
    }

    private var internalDriveMode = InternalModes.AUTO_ROADRUNNER_CONTROL
    private enum class InternalModes {
        MANUAL_CONTROL,
        AUTO_CUSTOM_CONTROL,
        AUTO_ROADRUNNER_CONTROL,
        NOT_DRIVING
    }

    // private methods
    private fun getVelocityConstraint(maxVel: Double, maxAngularVel: Double, trackWidth: Double): TrajectoryVelocityConstraint {
        return MinVelocityConstraint(
            Arrays.asList(
                AngularVelocityConstraint(maxAngularVel),
                MecanumVelocityConstraint(maxVel, trackWidth)
            )
        )
    }

    private fun getAccelerationConstraint(maxAccel: Double): TrajectoryAccelerationConstraint {
        return ProfileAccelerationConstraint(maxAccel)
    }

    // public methods
    fun trajectoryBuilder(startPose: Pose2d): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startPose.heading, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, reversed, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startHeading, VEL_CONSTRAINT, ACCEL_CONSTRAINT)
    }

    fun trajectorySequenceBuilder(startPose: Pose2d): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            VEL_CONSTRAINT, ACCEL_CONSTRAINT,
            DriveConstants.MAX_ANG_VEL, DriveConstants.MAX_ANG_ACCEL
        )
    }

    fun followTrajectorySequenceAsync(trajectorySequence: TrajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence)
    }

    fun followTrajectoryAsync(trajectory: Trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(trajectory.start())
                .addTrajectory(trajectory)
                .build()
        )
    }

    // override methods
    // subsystem methods
    override fun update() {
        updatePose()
        heading = Angle(poseEstimate.heading, AngleUnit.RAD).wrap()
        NakiriDashboard["heading"] = heading.deg

        if (internalDriveMode == InternalModes.AUTO_ROADRUNNER_CONTROL && currentTrajectorySequence != null) {
            followTrajectorySequenceAsync(currentTrajectorySequence!!)
            val signal: DriveSignal? = trajectorySequenceRunner.update(poseEstimate, poseVelocity)
            if (signal != null) setDriveSignal(signal)
        }

        // set wheel to motor
        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!
        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        ultrasonics.sendDashboardPacket(debugging)
        if (debugging) {
            NakiriDashboard.setHeader("ayame")
            NakiriDashboard["wheel powers"] = wheels
            motors.forEach { it.sendDataToDashboard() }
        }
    }

    override fun reset() {
        setVectorPower(Pose.DEFAULT_RAW)
    }

    // roadrunner implementation methods
    override val rawExternalHeading: Double
        get() = imuOffsetRead

    override fun getWheelPositions(): List<Double> {
        return motors.map { DriveConstants.encoderTicksToInches(it.position.d) } // todo fix to bulk
    }

    override fun getWheelVelocities(): List<Double> {
        return motors.map { DriveConstants.encoderTicksToInches(it.velocity) }
    }

    override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        wheels = mutableListOf(frontLeft, rearLeft, frontRight, rearRight)
    }

    val externalHeadingVelocity: Double
        get() = imu.angularVelocity.zRotationRate.d

    init {
        follower = HolonomicPIDVAFollower(
            TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
            Pose2d(0.5, 0.5, Math.toRadians(0.5)), 0.5
        )

        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.loggingEnabled = false
        imu.initialize(parameters)
        remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN)

        headingOffset = imu.angularOrientation.firstAngle.toDouble()
        thirdHeadingOffset = imu.angularOrientation.thirdAngle.toDouble()

        batteryVoltageSensor = BulkDataManager.hwMap.voltageSensor.iterator().next()

        trajectorySequenceRunner = TrajectorySequenceRunner(follower, HEADING_PID)
    }

    /**
     *
     * LOCALIZER
     */

    private var goingIntoPipesFrom = LocationStates.FIELD
    private var locationState = LocationStates.FIELD
    private var pipeTimer = ElapsedTime()
    private enum class LocationStates {
        CRATER,
        PIPES,
        FIELD,
        NONE,
    }

    private var pipeState = PipeStates.FRONT_OVER_FIRST
    private enum class PipeStates {
        FRONT_OVER_FIRST,
        FRONT_OVER_SECOND,
        BACK_OVER_FIRST,
        BACK_OVER_SECOND
    }

    private var lastUltrasonicXValue = 0.0
    private var lastUltrasonicYValue = 0.0

    private fun updatePose() {

        val upAngleVal = 10.0
        val downAngleVal = 360.0 - 10.0

        NakiriDashboard.setHeader("localizer")
        NakiriDashboard["current third angle"] = imuThirdAngle

        // add another check for if pose estimate is close to pipes
        // or add a check to see if trajectory is going to cross pipes (boolean in class)
        val intakeAnglingDown = imuThirdAngle > upAngleVal && imuThirdAngle < 180.0
        val intakeAnglingUp = imuThirdAngle < downAngleVal && imuThirdAngle > 180.0
        val flat = !intakeAnglingDown && !intakeAnglingUp

        NakiriDashboard["going up"] = intakeAnglingDown
        NakiriDashboard["going down"] = intakeAnglingUp

        val shouldDefault = flat && locationState != LocationStates.PIPES

        NakiriDashboard["should use normal localizer"] = shouldDefault

        if (shouldDefault) {
            updatePoseEstimate()

            if (goingIntoPipesFrom != LocationStates.NONE) {
                goingIntoPipesFrom = LocationStates.NONE
            }
        } else {
            /**
             *
             * possibilities:
             * 1. in field, going over pipe first bump (front wheels)
             * 2. in pipe/field, going over pipe second bump (front wheels)
             * 3. in crater/pipe/field, going over pipe first bump (back wheels)
             * 4. in crater/pipe, going over pipe second bump (back wheels)
             *
             *
             * 5. in crater, going over pipe first bump (back wheels)
             * 6. in pipe/crater, going over pipe second bump (back wheels)
             * 7. in pipe/crater/field, going over pipe first bump (front wheels)
             * 8. in pipe/field, going over pipe second bump (front wheels)
             *
             */

            // first check if we are going into pipes when fully in field / crater
            if (locationState != LocationStates.PIPES) {
                locationState = LocationStates.PIPES
            }

            if (goingIntoPipesFrom == LocationStates.NONE) {
                if (locationState == LocationStates.FIELD) {
                    pipeState = PipeStates.FRONT_OVER_FIRST
                } else {
                    pipeState = PipeStates.BACK_OVER_FIRST
                }
            }

            if (goingIntoPipesFrom == LocationStates.FIELD) {
                when (pipeState) {
                    PipeStates.FRONT_OVER_FIRST -> {
                        pipeTimer.reset()
                        if (flat) {
                            pipeState = PipeStates.FRONT_OVER_SECOND
                        }
                    }

                    PipeStates.FRONT_OVER_SECOND -> {
                        if (flat) {
                            pipeState = PipeStates.BACK_OVER_FIRST
                        }
                    }

                    PipeStates.BACK_OVER_FIRST -> {
                        if (flat) {
                            pipeState = PipeStates.BACK_OVER_SECOND
                        }
                    }

                    PipeStates.BACK_OVER_SECOND -> {
                        if (flat) {
                            pipeState = PipeStates.BACK_OVER_FIRST
                            goingIntoPipesFrom = LocationStates.FIELD
                            finishedTurning = false
                            relocalize()
                        }
                    }
                }
            } else if (goingIntoPipesFrom == LocationStates.CRATER) {
                when (pipeState) {
                    PipeStates.BACK_OVER_FIRST -> {
                        pipeTimer.reset()
                        if (flat) {
                            pipeState = PipeStates.BACK_OVER_SECOND
                        }
                    }

                    PipeStates.BACK_OVER_SECOND -> {
                        if (flat) {
                            pipeState = PipeStates.FRONT_OVER_FIRST
                        }
                    }

                    PipeStates.FRONT_OVER_FIRST -> {
                        if (flat) {
                            pipeState = PipeStates.FRONT_OVER_SECOND
                        }
                    }

                    PipeStates.FRONT_OVER_SECOND -> {
                        if (flat) {
                            pipeState = PipeStates.FRONT_OVER_FIRST
                            goingIntoPipesFrom = LocationStates.FIELD
                            finishedTurning = false
                            relocalize()
                        }
                    }
                }
            }
        }

        // will only ping if isReading (startReading() has been called)
        ultrasonics.update()
    }

    private var finishedTurning = false
    private fun turnToZero() {
        val currentHeading = Angle(imuOffsetRead, AngleUnit.RAD).wrap()
        val targetHeading = Angle(0.0, AngleUnit.RAD)
        val deltaH = (targetHeading - currentHeading).wrap().angle

        val turnPower = if (deltaH < 2.0.radians) {
            finishedTurning = true
            ultrasonics.startReading()
            0.0
        } else {
            deltaH / 90.0.radians
        }

        setVectorPower(Pose(Point.ORIGIN, Angle(turnPower, AngleUnit.RAW)))
    }

    private var lastRelocalizedPose = Pose(Point.ORIGIN, Angle.EAST)
    private fun relocalize() {
        // rotate robot so that we are facing minerals (wall)
        // once we do that just find deltas and yeah

        // rotate
        if (!finishedTurning) {
            turnToZero()
        }

        // TODO: FIX HOW THIS SHIT WORKS !!!!!!!!!!
        if (ultrasonics.hasBeenRead && ultrasonics.finishedReadInterval) {
            // TODO ::::::::::::::::::: CHANGE IT TO TRIG CALC TO ACCOUNT FOR VERY SLIGHT DISCREPANCIES CRINGE!

            lastUltrasonicXValue = ultrasonics.horizontalReading.d
            lastUltrasonicYValue = ultrasonics.forwardReading.d

            // find deltas
            val y = 72.0 - lastUltrasonicYValue

            val x = if (Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
                -72.0 + lastUltrasonicXValue
            } else {
                72.0 - lastUltrasonicXValue
            }

            lastRelocalizedPose = Pose(Point(x, y), Angle.EAST)
        }
    }

    /**
     * cross pipes fully from either crater -> field or field -> crater
     */
    private var currentTrajectorySequence: TrajectorySequence? = null
    private lateinit var redFieldToPrePipeTrajSequence: TrajectorySequence
    private val fieldCross = Pose2d(6.0, -36.0, Math.toRadians(180.0))
    private fun crossCraterToField() {
    }

    private val crossFieldToCraterStateMachine = StateMachineBuilder<LocationStates>()
        .state(LocationStates.FIELD)
        .onEnter {
            internalDriveMode = InternalModes.AUTO_ROADRUNNER_CONTROL
            redFieldToPrePipeTrajSequence = trajectorySequenceBuilder(poseEstimate)
                .lineToLinearHeading(fieldCross)
                .build()
        }
        .loop { followTrajectorySequenceAsync(redFieldToPrePipeTrajSequence) }
        .transition { !trajectorySequenceRunner.isBusy }
        .state(LocationStates.PIPES)
        .onEnter {
            internalDriveMode = InternalModes.AUTO_CUSTOM_CONTROL
            setVectorPower(Pose(Point(0.0, 1.0), Angle(0.0, AngleUnit.RAW)))
        }
        .transition { locationState == LocationStates.CRATER }
        .onExit { setVectorPower(Pose.DEFAULT_RAW) }
        .build()

    private fun crossFieldToCrater() {
        if (!crossFieldToCraterStateMachine.running) {
            crossFieldToCraterStateMachine.reset()
        }

        crossFieldToCraterStateMachine.update()
    }
}
