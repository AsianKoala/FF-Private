package robotuprising.ftc2021.auto.drive.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotuprising.ftc2021.auto.drive.SampleMecanumDrive

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
class StraightTest : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry())
        val drive = SampleMecanumDrive(hardwareMap)
        val trajectory: Trajectory = drive.trajectoryBuilder(Pose2d())
            .forward(DISTANCE)
            .build()
        waitForStart()
        if (isStopRequested()) return
        drive.followTrajectory(trajectory)
        val poseEstimate: Pose2d = drive.poseEstimate
        telemetry.addData("finalX", poseEstimate.x)
        telemetry.addData("finalY", poseEstimate.y)
        telemetry.addData("finalHeading", poseEstimate.heading)
        telemetry.update()
        while (!isStopRequested() && opModeIsActive());
    }

    companion object {
        var DISTANCE = 60.0 // in
    }
}
