package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.sequences.teleop.DepositSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.sequences.teleop.IntakeSequence
import asiankoala.ftc2021.commands.subsystem.DuckCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.MecanumDriveCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger

open class HutaoTeleOp(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private var strategy = alliance.decide(Strategy.ALLIANCE_BLUE, Strategy.ALLIANCE_RED)

    override fun mInit() {
        hutao = Hutao(Pose(heading = 90.0.radians))
        bindDrive()
        bindDuck()
        bindCycling()
        bindStrategy()
    }

    private fun bindDrive() {
        hutao.drive.setDefaultCommand(
            MecanumDriveCommand(
                hutao.drive,
                driver.leftStick,
                driver.rightStick,
                1.0, 1.0, 1.0,
                xScalar = 0.7, yScalar = 0.7, rScalar = 0.7
            )
        )
    }

    private fun bindDuck() {
        driver.dpadUp.onPress(DuckCommands.DuckSpinSequence(hutao.duck, alliance))
    }

    private fun bindCycling() {
        driver.rightTrigger.onPress(IntakeSequence(::strategy, hutao.intake, hutao.outtake, hutao.indexer, hutao.turret, hutao.arm))

        val depositCommand = SequentialCommandGroup(
            DepositSequence(::strategy, hutao.slides, hutao.indexer, driver.leftTrigger::isJustPressed),
            HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.encoders.slideEncoder)
        )
        CommandScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && !depositCommand.isScheduled }, depositCommand)

    }

    private fun bindStrategy() {
        driver.leftBumper.onPress { strategy = Strategy.ALLIANCE_BLUE }
        driver.rightBumper.onPress { strategy = Strategy.SHARED_BLUE }
        driver.x.onPress { strategy = Strategy.ALLIANCE_RED }
        driver.b.onPress { strategy = Strategy.SHARED_RED }
    }

    override fun mLoop() {
        Logger.addTelemetryData("strategy", strategy)
        Logger.addTelemetryData("target angle", strategy.getTurretAngle())
        Logger.addTelemetryData("target inches", strategy.getSlideInches())
        Logger.addTelemetryData("position", hutao.drive.position)
        Logger.addTelemetryData("power", hutao.drive.powers.rawString())
        Logger.addTelemetryData("turret angle", hutao.encoders.turretEncoder.position)
        Logger.addTelemetryData("slides inches", hutao.encoders.slideEncoder.position)
    }
}

