package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoDepositSequence
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.GoToPointCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger
import com.asiankoala.koawalib.util.LoggerConfig
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

abstract class HutaoAuto(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    private fun setupTurretAndSlides() {
        hutao.slides.setPIDTarget(0.0)
        hutao.turret.setPIDTarget(180.0)
        hutao.slides.disabled = false
        hutao.turret.disabled = false
    }

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false, isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        hutao = Hutao()
        hutao.drive.setStartPose(Pose(6.0, 64.0, 0.0))
        setupTurretAndSlides()

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(alliance, driver.rightTrigger, hutao.outtake, hutao.arm, hutao.turret),
                AutoDepositSequence(hutao.slides, hutao.indexer),
//                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.encoders.slideEncoder),
//                GoToPointCommand(
//                        hutao.drive,
//                        Pose(56.0, 64.0, 0.0),
//                        1.0,
//                        1.0.radians,
//                        true,
//                        0.8,
//                        0.8,
//                        40.0.radians,
//                        20.0.radians,
//                        0.2
//                )
        )
    }

    override fun mStart() {
        mainCommand.schedule()
    }

    override fun mLoop() {
        if(mainCommand.isFinished) {
            requestOpModeStop()
        }
    }
}

@Autonomous
class HutaoBlueAuto : HutaoAuto(Alliance.BLUE)

@Autonomous
class HutaoRedAuto : HutaoAuto(Alliance.RED)