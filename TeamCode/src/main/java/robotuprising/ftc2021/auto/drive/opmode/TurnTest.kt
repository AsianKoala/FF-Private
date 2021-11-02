package robotuprising.ftc2021.auto.drive.opmode

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotuprising.ftc2021.auto.drive.SampleMecanumDrive
import kotlin.Throws

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
@Autonomous(group = "drive")
class TurnTest : LinearOpMode() {
    @Throws(InterruptedException::class)
    override fun runOpMode() {
        val drive = SampleMecanumDrive(hardwareMap)
        waitForStart()
        if (isStopRequested) return
        drive.turn(Math.toRadians(ANGLE))
    }

    companion object {
        var ANGLE = 90.0 // deg
    }
}
