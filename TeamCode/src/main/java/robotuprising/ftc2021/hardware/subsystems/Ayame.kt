package robotuprising.ftc2021.hardware.subsystems

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
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import robotuprising.ftc2021.auto.drive.DriveConstants
import robotuprising.ftc2021.auto.drive.SampleMecanumDrive
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequence
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceBuilder
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceRunner
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.hardware.AxesSigns
import robotuprising.lib.hardware.BNO055IMUUtil.remapAxes
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d
import java.util.*
import kotlin.math.absoluteValue

class Ayame :
    MecanumDrive(
        DriveConstants.kV,
        DriveConstants.kA,
        DriveConstants.kStatic,
        DriveConstants.TRACK_WIDTH
    ),
    Subsystem {

    // fields
    private val fl = NakiriMotor("FL", true).brake.openLoopControl
    private val bl = NakiriMotor("BL", true).brake.openLoopControl
    private val fr = NakiriMotor("FR", true).brake.openLoopControl
    private val br = NakiriMotor("BR", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

    private val imu = BulkDataManager.hwMap[BNO055IMUImpl::class.java, "imu"]
    private val headingOffset: Double
    private val imuOffsetRead: Double get() = imu.angularOrientation.firstAngle - headingOffset

    private var wheels: List<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0)

    fun setVectorPower(powers: Pose) {
        wheels = mutableListOf(
            -powers.y - powers.x - powers.h.angle,
            -powers.y + powers.x - powers.h.angle,
            powers.y - powers.x - powers.h.angle,
            powers.y + powers.x - powers.h.angle
        )
    }

    // roadrunner class methods

    private enum class InternalModes {
        MANUAL_CONTROL,
        AUTO_CUSTOM_CONTROL,
        AUTO_ROADRUNNER_CONTROL
    }

    private enum class RoadRunnerModes {
        IDLE, TURN, FOLLOW_TRAJECTORY
    }

    private val trajectorySequenceRunner: TrajectorySequenceRunner
    private val follower: TrajectoryFollower
    private val velConstraint: TrajectoryVelocityConstraint
    private val accelConstraint: TrajectoryAccelerationConstraint
    private val poseHistory: LinkedList<Pose2d>
    private val batteryVoltageSensor: VoltageSensor
    private lateinit var lastPoseOnTurn: Pose2d

    fun trajectoryBuilder(startPose: Pose2d): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startPose.heading, velConstraint, accelConstraint)
    }

    fun trajectoryBuilder(startPose: Pose2d, reversed: Boolean): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, reversed, velConstraint, accelConstraint)
    }

    fun trajectoryBuilder(startPose: Pose2d, startHeading: Double): TrajectoryBuilder {
        return TrajectoryBuilder(startPose, startHeading, velConstraint, accelConstraint)
    }

    fun trajectorySequenceBuilder(startPose: Pose2d): TrajectorySequenceBuilder {
        return TrajectorySequenceBuilder(
            startPose,
            velConstraint, accelConstraint,
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

    var following = false
    var heading = Angle(0.0, AngleUnit.RAD)

    // subsystem implementation methods
    override fun update() {
        updatePoseEstimate()
        heading = Angle(poseEstimate.heading, AngleUnit.RAD).wrap()
        NakiriDashboard["heading"] = heading.deg

        if (following) {
            val signal: DriveSignal? = trajectorySequenceRunner.update(poseEstimate, poseVelocity)
            if (signal != null) setDriveSignal(signal)
        }

        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!

        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard.setHeader("ayame")
        NakiriDashboard["wheel powers"] = wheels

        if (debugging) {
            motors.forEach { it.sendDataToDashboard() }
        }
    }

    override fun stop() {
    }

    // roadrunner implementation methods
    override val rawExternalHeading: Double
        get() = imuOffsetRead

    override fun getWheelPositions(): List<Double> {
        return motors.map { it.position.d } // todo fix to bulk
    }

    override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        wheels = mutableListOf(frontLeft, rearLeft, frontRight, rearRight)
    }

    companion object {
        var TRANSLATIONAL_PID = PIDCoefficients(0.0, 0.0, 0.0)
        var HEADING_PID = PIDCoefficients(0.0, 0.0, 0.0)

        fun getVelocityConstraint(maxVel: Double, maxAngularVel: Double, trackWidth: Double): TrajectoryVelocityConstraint {
            return MinVelocityConstraint(
                Arrays.asList(
                    AngularVelocityConstraint(maxAngularVel),
                    MecanumVelocityConstraint(maxVel, trackWidth)
                )
            )
        }

        fun getAccelerationConstraint(maxAccel: Double): TrajectoryAccelerationConstraint {
            return ProfileAccelerationConstraint(maxAccel)
        }
    }

    init {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.loggingEnabled = false
        imu.initialize(parameters)
        remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN)
        headingOffset = imu.angularOrientation.firstAngle.toDouble()

        velConstraint = getVelocityConstraint(DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH)
        accelConstraint = getAccelerationConstraint(DriveConstants.MAX_ACCEL)
        follower = HolonomicPIDVAFollower(
            TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
            Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5
        )
        poseHistory = LinkedList()
        batteryVoltageSensor = BulkDataManager.hwMap.voltageSensor.iterator().next()

        trajectorySequenceRunner = TrajectorySequenceRunner(follower, SampleMecanumDrive.HEADING_PID)
    }
}
