package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.*
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
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

abstract class HutaoAuto(private val alliance: Alliance, private val startPose: Pose) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false, isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        hutao = Hutao(startPose)

        val initialIntakeX = 56.0

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(alliance, driver.rightTrigger, hutao.outtake, hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                AutoDepositSequence(hutao.slides, hutao.indexer),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer,
                        hutao.arm, hutao.encoders.slideEncoder),

                GoToPointCommand(
                        hutao.drive,
                        Pose(initialIntakeX, alliance.decide(64.0, -64.0), 0.0),
                        distTol = 1.5,
                        angleTol = 1.5.radians,
                        stop = true,
                ).raceWith(AutoIntakeSequence(hutao.intake)),

                AutoCockSequence(alliance, hutao.intake, hutao.outtake, hutao.indexer, hutao.turret, hutao.arm)



//                CycleSequence(alliance, hutao.drive, initialIntakeX, hutao.intake,
//                        hutao.outtake, hutao.indexer, hutao.turret,
//                        hutao.arm, startPose, hutao.slides, hutao.encoders.slideEncoder)
        )

        mainCommand.schedule()
    }

    override fun mInitLoop() {
        super.mInitLoop()
        hutao.log()
    }

    override fun mLoop() {
        hutao.log()
    }
}

@Autonomous
class HutaoBlueAuto : HutaoAuto(Alliance.BLUE, Pose(16.0, 64.0, 0.0))

@Autonomous
class HutaoRedAuto : HutaoAuto(Alliance.RED, Pose(16.0, -64.0, 0.0))