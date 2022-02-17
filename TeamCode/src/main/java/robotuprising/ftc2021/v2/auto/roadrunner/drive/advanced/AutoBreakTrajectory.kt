package robotuprising.ftc2021.v2.auto.roadrunner.drive.advanced

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.Throws

/**
 * Example opmode demonstrating how to break out from a live trajectory at any arbitrary point in
 * time. This will allow you to do some cool things like incorporating live trajectory following in
 * your teleop. Check out TeleOpAgumentedDriving.java for an example of such behavior.
 *
 *
 * 3 seconds into the start of the opmode, `drive.cancelFollowing()` is called, forcing the drive
 * mode into IDLE and prematurely stopping the following.
 *
 *
 * This sample utilizes the SampleMecanumDriveCancelable.java class.
 */
@Autonomous(group = "advanced")
@Disabled
class AutoBreakTrajectory : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        // Initialize custom cancelable SampleMecanumDrive class
        val drive = SampleMecanumDriveCancelable(hardwareMap)

        // Set the pose estimate to where you know the bot will start in autonomous
        // Refer to https://www.learnroadrunner.com/trajectories.html#coordinate-system for a map
        // of the field
        // This example sets the bot at x: -20, y: -35, and facing 90 degrees (turned counter-clockwise)
        val startPose = Pose2d(-20.0, -35.0, Math.toRadians(90.0))
        drive.poseEstimate = startPose
        waitForStart()
        if (isStopRequested) return

        // Example spline path from SplineTest.java
        val traj = drive.trajectoryBuilder(startPose)
            .splineTo(Vector2d(60.0, 60.0), 0.0)
            .build()

        // We follow the trajectory asynchronously so we can run our own logic
        drive.followTrajectoryAsync(traj)

        // Start the timer so we know when to cancel the following
        val stopTimer = ElapsedTime()
        while (opModeIsActive() && !isStopRequested) {
            // 3 seconds into the opmode, we cancel the following
            if (stopTimer.seconds() >= 3) {
                // Cancel following
                drive.cancelFollowing()

                // Stop the motors
                drive.setDrivePower(Pose2d())
            }

            // Update drive
            drive.update()
        }
    }
}
