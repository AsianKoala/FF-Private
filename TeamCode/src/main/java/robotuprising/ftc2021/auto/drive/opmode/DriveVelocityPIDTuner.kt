
package robotuprising.ftc2021.auto.drive.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator.generateSimpleMotionProfile
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.util.NanoClock
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.RobotLog
import robotuprising.ftc2021.auto.drive.DriveConstants.MAX_ACCEL
import robotuprising.ftc2021.auto.drive.DriveConstants.MAX_VEL
import robotuprising.ftc2021.auto.drive.DriveConstants.MOTOR_VELO_PID
import robotuprising.ftc2021.auto.drive.DriveConstants.RUN_USING_ENCODER
import robotuprising.ftc2021.auto.drive.DriveConstants.kV
import robotuprising.ftc2021.auto.drive.SampleMecanumDrive
import robotuprising.lib.util.Extensions.d

 /*
 * This routine is designed to tune the PID coefficients used by the REV Expansion Hubs for closed-
 * loop velocity control. Although it may seem unnecessary, tuning these coefficients is just as
 * important as the positional parameters. Like the other manual tuning routines, this op mode
 * relies heavily upon the dashboard. To access the dashboard, connect your computer to the RC's
 * WiFi network. In your browser, navigate to https://192.168.49.1:8080/dash if you're using the RC
 * phone or https://192.168.43.1:8080/dash if you are using the Control Hub. Once you've successfully
 * connected, start the program, and your robot will begin moving forward and backward according to
 * a motion profile. Your job is to graph the velocity errors over time and adjust the PID
 * coefficients (note: the tuning variable will not appear until the op mode finishes initializing).
 * Once you've found a satisfactory set of gains, add them to the DriveConstants.java file under the
 * MOTOR_VELO_PID field.
 *
 * Recommended tuning process:
 *
 * 1. Increase kP until any phase lag is eliminated. Concurrently increase kD as necessary to
 *    mitigate oscillations.
 * 2. Add kI (or adjust kF) until the steady state/constant velocity plateaus are reached.
 * 3. Back off kP and kD a little until the response is less oscillatory (but without lag).
 *
 * Pressing Y/Δ (Xbox/PS4) will pause the tuning process and enter driver override, allowing the
 * user to reset the position of the bot in the event that it drifts off the path.
 * Pressing B/O (Xbox/PS4) will cede control back to the tuning process.
 */
@Config
@Autonomous(group = "drive")
@Disabled
class DriveVelocityPIDTuner : LinearOpMode() {
    internal enum class Mode {
        DRIVER_MODE, TUNING_MODE
    }

    override fun runOpMode() {
        if (!RUN_USING_ENCODER) {
            RobotLog.setGlobalErrorMsg(
                "%s does not need to be run if the built-in motor velocity" +
                    "PID is not in use",
                javaClass.simpleName
            )
        }
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry())
        val drive = SampleMecanumDrive(hardwareMap)
        var mode = Mode.TUNING_MODE
        var lastKp: Double = MOTOR_VELO_PID.p
        var lastKi: Double = MOTOR_VELO_PID.i
        var lastKd: Double = MOTOR_VELO_PID.d
        var lastKf: Double = MOTOR_VELO_PID.f
        drive.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID)
        val clock: NanoClock = NanoClock.system()
        telemetry.addLine("Ready!")
        telemetry.update()
        telemetry.clearAll()
        waitForStart()
        if (isStopRequested()) return
        var movingForwards = true
        var activeProfile: MotionProfile = generateProfile(true)
        var profileStart: Double = clock.seconds()
        while (!isStopRequested()) {
            telemetry.addData("mode", mode)
            when (mode) {
                Mode.TUNING_MODE -> {
                    if (gamepad1.y) {
                        mode = Mode.DRIVER_MODE
                        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
                    }

                    // calculate and set the motor power
                    val profileTime: Double = clock.seconds() - profileStart
                    if (profileTime > activeProfile.duration()) {
                        // generate a new profile
                        movingForwards = !movingForwards
                        activeProfile = generateProfile(movingForwards)
                        profileStart = clock.seconds()
                    }
                    val motionState: MotionState = activeProfile.get(profileTime)
                    val targetPower: Double = kV * motionState.v
                    drive.setDrivePower(Pose2d(targetPower, 0.0, 0.0))
                    val velocities: List<Double> = drive.getWheelVelocities()

                    // update telemetry
                    telemetry.addData("targetVelocity", motionState.v)
                    var i = 0
                    while (i < velocities.size) {
                        telemetry.addData("measuredVelocity$i", velocities[i])
                        telemetry.addData(
                            "error$i",
                            motionState.v - velocities[i]
                        )
                        i++
                    }
                }
                Mode.DRIVER_MODE -> {
                    if (gamepad1.b) {
                        drive.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
                        mode = Mode.TUNING_MODE
                        movingForwards = true
                        activeProfile = generateProfile(movingForwards)
                        profileStart = clock.seconds()
                    }
                    drive.setWeightedDrivePower(
                        Pose2d(
                            -gamepad1.left_stick_y.d,
                            -gamepad1.left_stick_x.d,
                            -gamepad1.right_stick_x.d
                        )
                    )
                }
            }
            if (lastKp != MOTOR_VELO_PID.p || lastKd != MOTOR_VELO_PID.d || lastKi != MOTOR_VELO_PID.i || lastKf != MOTOR_VELO_PID.f) {
                drive.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, MOTOR_VELO_PID)
                lastKp = MOTOR_VELO_PID.p
                lastKi = MOTOR_VELO_PID.i
                lastKd = MOTOR_VELO_PID.d
                lastKf = MOTOR_VELO_PID.f
            }
            telemetry.update()
        }
    }

    companion object {
        var DISTANCE = 72.0 // in
        private fun generateProfile(movingForward: Boolean): MotionProfile {
            val start = if (movingForward) {
                MotionState(DISTANCE, 0.0, 0.0, 0.0)
            } else {
                MotionState(0.0, 0.0, 0.0, 0.0)
            }
            val goal = if (movingForward) {
                MotionState(DISTANCE, 0.0, 0.0, 0.0)
            } else {
                MotionState(0.0, 0.0, 0.0, 0.0)
            }
            return generateSimpleMotionProfile(start, goal, MAX_VEL, MAX_ACCEL)
        }
    }
}
