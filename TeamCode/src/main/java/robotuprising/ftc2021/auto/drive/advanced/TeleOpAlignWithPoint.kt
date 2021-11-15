//package robotuprising.ftc2021.auto.drive.advanced
//
//import robotuprising.ftc2021.auto.util.DashboardUtil.drawRobot
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
//import com.acmerobotics.roadrunner.geometry.Vector2d
//import kotlin.Throws
//import com.qualcomm.robotcore.hardware.DcMotor
//import robotuprising.ftc2021.auto.drive.advanced.PoseStorage
//import com.acmerobotics.roadrunner.geometry.Pose2d
//import com.acmerobotics.dashboard.telemetry.TelemetryPacket
//import robotuprising.ftc2021.auto.drive.DriveConstants
//import com.acmerobotics.dashboard.FtcDashboard
//import com.acmerobotics.dashboard.config.Config
//import com.acmerobotics.roadrunner.control.PIDFController
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp
//import robotuprising.ftc2021.auto.drive.SampleMecanumDrive
//
///**
// * This opmode demonstrates how one would implement "align to point behavior" in teleop. You specify
// * a desired vector (x/y coordinate) via`targetPosition`. In the `ALIGN_TO_POINT` mode, the bot will
// * switch into field centric control and independently control its heading to align itself with the
// * specified `targetPosition`.
// *
// *
// * Press `a` to switch into alignment mode and `b` to switch back into standard teleop driving mode.
// *
// *
// * Note: We don't call drive.update() here because it has its own field drawing functions. We don't
// * want that to interfere with our graph so we just directly update localizer instead
// */
//@Config
//@TeleOp(group = "advanced")
//class TeleOpAlignWithPoint : LinearOpMode() {
//    // Define 2 states, driver control or alignment control
//    internal enum class Mode {
//        NORMAL_CONTROL, ALIGN_TO_POINT
//    }
//
//    private var currentMode = Mode.NORMAL_CONTROL
//
//    // Declare a PIDF Controller to regulate heading
//    // Use the same gains as SampleMecanumDrive's heading controller
//    private val headingController = PIDFController(SampleMecanumDrive.HEADING_PID)
//
//    // Declare a target vector you'd like your bot to align with
//    // Can be any x/y coordinate of your choosing
//    private val targetPosition = Vector2d(0.0, 0.0)
//    @Throws(InterruptedException::class)
//    override fun runOpMode() {
//        // Initialize SampleMecanumDrive
//        val drive = SampleMecanumDrive(hardwareMap)
//
//        // We want to turn off velocity control for teleop
//        // Velocity control per wheel is not necessary outside of motion profiled auto
//        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER)
//
//        // Retrieve our pose from the PoseStorage.currentPose static field
//        // See AutoTransferPose.java for further details
//        drive.localizer.poseEstimate = PoseStorage.currentPose
//
//        // Set input bounds for the heading controller
//        // Automatically handles overflow
//        headingController.setInputBounds(-Math.PI, Math.PI)
//        waitForStart()
//        if (isStopRequested) return
//        while (opModeIsActive() && !isStopRequested) {
//            // Read pose
//            val poseEstimate = drive.localizer.poseEstimate
//
//            // Declare a drive direction
//            // Pose representing desired x, y, and angular velocity
//            var driveDirection: Pose2d? = Pose2d()
//            telemetry.addData("mode", currentMode)
//
//            // Declare telemetry packet for dashboard field drawing
//            val packet = TelemetryPacket()
//            val fieldOverlay = packet.fieldOverlay()
//            when (currentMode) {
//                Mode.NORMAL_CONTROL -> {
//                    // Switch into alignment mode if `a` is pressed
//                    if (gamepad1.a) {
//                        currentMode = Mode.ALIGN_TO_POINT
//                    }
//
//                    // Standard teleop control
//                    // Convert gamepad input into desired pose velocity
//                    driveDirection = Pose2d(
//                            (-gamepad1.left_stick_y).toDouble(),
//                            (-gamepad1.left_stick_x).toDouble(),
//                            (-gamepad1.right_stick_x).toDouble()
//                    )
//                }
//                Mode.ALIGN_TO_POINT -> {
//                    // Switch back into normal driver control mode if `b` is pressed
//                    if (gamepad1.b) {
//                        currentMode = Mode.NORMAL_CONTROL
//                    }
//
//                    // Create a vector from the gamepad x/y inputs which is the field relative movement
//                    // Then, rotate that vector by the inverse of that heading for field centric control
//                    val fieldFrameInput = Vector2d(
//                            (-gamepad1.left_stick_y).toDouble(),
//                            (-gamepad1.left_stick_x).toDouble()
//                    )
//                    val robotFrameInput = fieldFrameInput.rotated(-poseEstimate.heading)
//
//                    // Difference between the target vector and the bot's position
//                    val difference = targetPosition.minus(poseEstimate.vec())
//                    // Obtain the target angle for feedback and derivative for feedforward
//                    val theta = difference.angle()
//
//                    // Not technically omega because its power. This is the derivative of atan2
//                    val thetaFF = -fieldFrameInput.rotated(-Math.PI / 2).dot(difference) / (difference.norm() * difference.norm())
//
//                    // Set the target heading for the heading controller to our desired angle
//                    headingController.targetPosition = theta
//
//                    // Set desired angular velocity to the heading controller output + angular
//                    // velocity feedforward
//                    val headingInput = ((headingController.update(poseEstimate.heading)
//                            * DriveConstants.kV + thetaFF)
//                            * DriveConstants.TRACK_WIDTH)
//
//                    // Combine the field centric x/y velocity with our derived angular velocity
//                    driveDirection = Pose2d(
//                            robotFrameInput,
//                            headingInput
//                    )
//
//                    // Draw the target on the field
//                    fieldOverlay.setStroke("#dd2c00")
//                    fieldOverlay.strokeCircle(targetPosition.x, targetPosition.y, DRAWING_TARGET_RADIUS)
//
//                    // Draw lines to target
//                    fieldOverlay.setStroke("#b89eff")
//                    fieldOverlay.strokeLine(targetPosition.x, targetPosition.y, poseEstimate.x, poseEstimate.y)
//                    fieldOverlay.setStroke("#ffce7a")
//                    fieldOverlay.strokeLine(targetPosition.x, targetPosition.y, targetPosition.x, poseEstimate.y)
//                    fieldOverlay.strokeLine(targetPosition.x, poseEstimate.y, poseEstimate.x, poseEstimate.y)
//                }
//            }
//
//            // Draw bot on canvas
//            fieldOverlay.setStroke("#3F51B5")
//            drawRobot(fieldOverlay, poseEstimate)
//            drive.setWeightedDrivePower(driveDirection)
//
//            // Update the heading controller with our current heading
//            headingController.update(poseEstimate.heading)
//
//            // Update he localizer
//            drive.localizer.update()
//
//            // Send telemetry packet off to dashboard
//            FtcDashboard.getInstance().sendTelemetryPacket(packet)
//
//            // Print pose to telemetry
//            telemetry.addData("x", poseEstimate.x)
//            telemetry.addData("y", poseEstimate.y)
//            telemetry.addData("heading", poseEstimate.heading)
//            telemetry.update()
//        }
//    }
//
//    companion object {
//        var DRAWING_TARGET_RADIUS = 2.0
//    }
//}