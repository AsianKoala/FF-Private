package robotuprising.ftc2021.auto.drive.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotuprising.ftc2021.auto.drive.SampleMecanumDrive
import kotlin.Throws

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(group = "drive")
@Disabled
class StrafeTest : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        val drive = SampleMecanumDrive(hardwareMap)
        val trajectory = drive.trajectoryBuilder(Pose2d())
            .strafeRight(DISTANCE)
            .build()
        waitForStart()
        if (isStopRequested) return
        drive.followTrajectory(trajectory)
        val poseEstimate = drive.poseEstimate
        telemetry.addData("finalX", poseEstimate.x)
        telemetry.addData("finalY", poseEstimate.y)
        telemetry.addData("finalHeading", poseEstimate.heading)
        telemetry.update()
        while (!isStopRequested && opModeIsActive());
    }

    companion object {
        var DISTANCE = 60.0 // in
    }
}
