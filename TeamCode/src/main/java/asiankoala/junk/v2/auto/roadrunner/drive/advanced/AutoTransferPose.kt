package asiankoala.junk.v2.auto.roadrunner.drive.advanced

import asiankoala.junk.v2.auto.roadrunner.drive.SampleMecanumDrive
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import kotlin.Throws

/**
 * Example opmode demonstrating how to hand-off the pose from your autonomous opmode to your teleop
 * by passing the data through a static class.
 *
 *
 * This is required if you wish to read the pose from odometry in teleop and you run an autonomous
 * sequence prior. Without passing the data between each other, teleop isn't completely sure where
 * it starts.
 *
 *
 * This example runs the same paths used in the SplineTest tuning opmode. After the trajectory
 * following concludes, it simply sets the static value, `PoseStorage.currentPose`, to the latest
 * localizer reading.
 * However, this method is not foolproof. The most immediate problem is that the pose will not be
 * written to the static field if the opmode is stopped prematurely. To work around this issue, you
 * need to continually write the pose to the static field in an async trajectory follower. A simple
 * example of async trajectory following can be found at
 * https://www.learnroadrunner.com/advanced.html#async-following
 * A more advanced example of async following can be found in the AsyncFollowingFSM.java class.
 *
 *
 * The other edge-case issue you may want to cover is saving the pose value to disk by writing it
 * to a file in the event of an app crash. This way, the pose can be retrieved and set even if
 * something disastrous occurs. Such a sample has not been included.
 */
@Disabled
@Autonomous(group = "advanced")
class AutoTransferPose : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        // Declare your drive class
        val drive = SampleMecanumDrive(hardwareMap)

        // Set the pose estimate to where you know the bot will start in autonomous
        // Refer to https://www.learnroadrunner.com/trajectories.html#coordinate-system for a map
        // of the field
        // This example sets the bot at x: 10, y: 15, and facing 90 degrees (turned counter-clockwise)
        val startPose = Pose2d(10.0, 15.0, Math.toRadians(90.0))
        drive.poseEstimate = startPose
        waitForStart()
        if (isStopRequested) return

        // Example spline path from SplineTest.java
        // Make sure the start pose matches with the localizer's start pose
        val traj = drive.trajectoryBuilder(startPose)
            .splineTo(Vector2d(45.0, 45.0), 0.0)
            .build()
        drive.followTrajectory(traj)
        sleep(2000)
        drive.followTrajectory(
            drive.trajectoryBuilder(traj.end(), true)
                .splineTo(Vector2d(15.0, 15.0), Math.toRadians(180.0))
                .build()
        )

        // Transfer the current pose to PoseStorage so we can use it in TeleOp
        PoseStorage.currentPose = drive.poseEstimate
    }
}
