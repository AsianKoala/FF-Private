package robotuprising.ftc2021.v2.auto.roadrunner.drive.advanced

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.v2.auto.roadrunner.drive.SampleMecanumDrive
import kotlin.Throws

/**
 * This opmode demonstrates how to create a teleop using just the SampleMecanumDrive class without
 * the need for an external robot class. This will allow you to do some cool things like
 * incorporating live trajectory following in your teleop. Check out TeleOpAgumentedDriving.java for
 * an example of such behavior.
 *
 *
 * This opmode is essentially just LocalizationTest.java with a few additions and comments.
 */
@TeleOp(group = "advanced")
@Disabled
class TeleOpDrive : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        // Initialize SampleMecanumDrive
        val drive = SampleMecanumDrive(hardwareMap)

        // We want to turn off velocity control for teleop
        // Velocity control per wheel is not necessary outside of motion profiled auto
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)

        // Retrieve our pose from the PoseStorage.currentPose static field
        // See AutoTransferPose.java for further details
        drive.poseEstimate = PoseStorage.currentPose
        waitForStart()
        if (isStopRequested) return
        while (opModeIsActive() && !isStopRequested) {
            drive.setWeightedDrivePower(
                Pose2d(
                    (-gamepad1.left_stick_y).toDouble(),
                    (-gamepad1.left_stick_x).toDouble(),
                    (-gamepad1.right_stick_x).toDouble()
                )
            )

            // Update everything. Odometry. Etc.
            drive.update()

            // Read pose
            val poseEstimate = drive.poseEstimate

            // Print pose to telemetry
            telemetry.addData("x", poseEstimate.x)
            telemetry.addData("y", poseEstimate.y)
            telemetry.addData("heading", poseEstimate.heading)
            telemetry.update()
        }
    }
}
