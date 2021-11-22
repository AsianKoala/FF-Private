
package robotuprising.ftc2021.auto.drive

import com.acmerobotics.dashboard.config.Config
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
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequence
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceBuilder
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequenceRunner
import robotuprising.ftc2021.auto.util.LynxModuleUtil
import robotuprising.lib.util.Extensions.d
import java.util.*

 /*
 * Simple mecanum drive hardware implementation for REV hardware.
 */
@Config
class SampleMecanumDrive(hardwareMap: HardwareMap) : MecanumDrive(DriveConstants.kV, DriveConstants.kA, DriveConstants.kStatic, DriveConstants.TRACK_WIDTH, DriveConstants.TRACK_WIDTH, LATERAL_MULTIPLIER) {
    private val trajectorySequenceRunner: TrajectorySequenceRunner
    private val follower: TrajectoryFollower
    private val leftFront: DcMotorEx
    private val leftRear: DcMotorEx
    private val rightRear: DcMotorEx
    private val rightFront: DcMotorEx
    private val motors: List<DcMotorEx>
    private val imu: BNO055IMU
    private val batteryVoltageSensor: VoltageSensor
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

    fun turnAsync(angle: Double) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(poseEstimate)
                .turn(angle)
                .build()
        )
    }

    fun turn(angle: Double) {
        turnAsync(angle)
        waitForIdle()
    }

    fun followTrajectoryAsync(trajectory: Trajectory) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(
            trajectorySequenceBuilder(trajectory.start())
                .addTrajectory(trajectory)
                .build()
        )
    }

    fun followTrajectory(trajectory: Trajectory) {
        followTrajectoryAsync(trajectory)
        waitForIdle()
    }

    fun followTrajectorySequenceAsync(trajectorySequence: TrajectorySequence) {
        trajectorySequenceRunner.followTrajectorySequenceAsync(trajectorySequence)
    }

    fun followTrajectorySequence(trajectorySequence: TrajectorySequence) {
        followTrajectorySequenceAsync(trajectorySequence)
        waitForIdle()
    }

    val lastError: Pose2d
        get() = trajectorySequenceRunner.lastPoseError

    fun update() {
        updatePoseEstimate()
        val signal: DriveSignal? = trajectorySequenceRunner.update(poseEstimate, poseVelocity)
        if (signal != null) setDriveSignal(signal)
    }

    fun waitForIdle() {
        while (!Thread.currentThread().isInterrupted && isBusy) update()
    }

    val isBusy: Boolean
        get() = trajectorySequenceRunner.isBusy

    fun setMode(runMode: DcMotor.RunMode) {
        for (motor in motors) {
            motor.setMode(runMode)
        }
    }

    fun setZeroPowerBehavior(zeroPowerBehavior: DcMotor.ZeroPowerBehavior) {
        for (motor in motors) {
            motor.setZeroPowerBehavior(zeroPowerBehavior)
        }
    }

    fun setPIDFCoefficients(runMode: DcMotor.RunMode, coefficients: PIDFCoefficients) {
        val compensatedCoefficients = PIDFCoefficients(
            coefficients.p, coefficients.i, coefficients.d,
            coefficients.f * 12 / batteryVoltageSensor.getVoltage()
        )
        for (motor in motors) {
            motor.setPIDFCoefficients(runMode, compensatedCoefficients)
        }
    }

    // todo look at
    fun setWeightedDrivePower(drivePower: Pose2d) {
        var vel: Pose2d = drivePower
        if ((
            Math.abs(drivePower.x) + Math.abs(drivePower.y) +
                Math.abs(drivePower.heading)
            ) > 1
        ) {
            // re-normalize the powers according to the weights
            val denom: Double = VX_WEIGHT * Math.abs(drivePower.x) + VY_WEIGHT * Math.abs(drivePower.y) + OMEGA_WEIGHT * Math.abs(drivePower.heading)
            vel = Pose2d(
                VX_WEIGHT * drivePower.x,
                VY_WEIGHT * drivePower.y,
                OMEGA_WEIGHT * drivePower.heading
            ).div(denom)
        }
        setDrivePower(vel)
    }

    override fun getWheelPositions(): List<Double> {
        val wP: MutableList<Double> = ArrayList()
        for (motor in motors) {
            wP.add(DriveConstants.encoderTicksToInches(motor.getCurrentPosition().toDouble()))
        }
        return wP
    }

    override fun getWheelVelocities(): List<Double> {
        val wV: MutableList<Double> = ArrayList()
        for (motor in motors) {
            wV.add(DriveConstants.encoderTicksToInches(motor.velocity))
        }
        return wV
    }

    override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        leftFront.power = frontLeft
        leftRear.power = rearLeft
        rightRear.power = rearRight
        rightFront.power = frontRight
    }

    override val rawExternalHeading: Double
        get() = imu.angularOrientation.firstAngle.d

    // TODO: This must be changed to match your configuration
    //                           | Z axis
    //                           |
    //     (Motor Port Side)     |   / X axis
    //                       ____|__/____
    //          Y axis     / *   | /    /|   (IO Side)
    //          _________ /______|/    //      I2C
    //                   /___________ //     Digital
    //                  |____________|/      Analog
    //
    //                 (Servo Port Side)
    //
    // The positive x axis points toward the USB port(s)
    //
    // Adjust the axis rotation rate as necessary
    // Rotate about the z axis is the default assuming your REV Hub/Control Hub is laying
    // flat on a surface
    val externalHeadingVelocity: Double
        get() = // TODO: This must be changed to match your configuration
            //                           | Z axis
            //                           |
            //     (Motor Port Side)     |   / X axis
            //                       ____|__/____
            //          Y axis     / *   | /    /|   (IO Side)
            //          _________ /______|/    //      I2C
            //                   /___________ //     Digital
            //                  |____________|/      Analog
            //
            //                 (Servo Port Side)
            //
            // The positive x axis points toward the USB port(s)
            //
            // Adjust the axis rotation rate as necessary
            // Rotate about the z axis is the default assuming your REV Hub/Control Hub is laying
            // flat on a surface
            imu.angularVelocity.zRotationRate.d

    companion object {
        var TRANSLATIONAL_PID = PIDCoefficients(0.0, 0.0, 0.0)
        var HEADING_PID = PIDCoefficients(0.0, 0.0, 0.0)
        var LATERAL_MULTIPLIER = 1.0
        var VX_WEIGHT = 1.0
        var VY_WEIGHT = 1.0
        var OMEGA_WEIGHT = 1.0
        private val VEL_CONSTRAINT: TrajectoryVelocityConstraint = getVelocityConstraint(DriveConstants.MAX_VEL, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH)
        private val ACCEL_CONSTRAINT: TrajectoryAccelerationConstraint = getAccelerationConstraint(DriveConstants.MAX_ACCEL)
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
        follower = HolonomicPIDVAFollower(
            TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID,
            Pose2d(0.5, 0.5, Math.toRadians(5.0)), 0.5
        )
        LynxModuleUtil.ensureMinimumFirmwareVersion(hardwareMap)
        batteryVoltageSensor = hardwareMap.voltageSensor.iterator().next()
        for (module in hardwareMap.getAll<LynxModule>(LynxModule::class.java)) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO)
        }

        // TODO: adjust the names of the following hardware devices to match your configuration
        imu = hardwareMap.get<BNO055IMU>(BNO055IMU::class.java, "imu")
        val parameters: BNO055IMU.Parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        imu.initialize(parameters)

        // TODO: if your hub is mounted vertically, remap the IMU axes so that the z-axis points
        // upward (normal to the floor) using a command like the following:
        // BNO055IMUUtil.remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN);
        leftFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "leftFront")
        leftRear = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "leftRear")
        rightRear = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "rightRear")
        rightFront = hardwareMap.get<DcMotorEx>(DcMotorEx::class.java, "rightFront")
        motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront)
        for (motor in motors) {
            val motorConfigurationType: MotorConfigurationType = motor.motorType.clone()
            motorConfigurationType.achieveableMaxRPMFraction = 1.0
            motor.setMotorType(motorConfigurationType)
        }
        if (DriveConstants.RUN_USING_ENCODER) {
            setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        }
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        if (DriveConstants.RUN_USING_ENCODER && DriveConstants.MOTOR_VELO_PID != null) {
            setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, DriveConstants.MOTOR_VELO_PID)
        }

        // TODO: reverse any motors using DcMotor.setDirection()

        // TODO: if desired, use setLocalizer() to change the localization method
        // for instance, setLocalizer(new ThreeTrackingWheelLocalizer(...));
        trajectorySequenceRunner = TrajectorySequenceRunner(follower, HEADING_PID)
    }
}
