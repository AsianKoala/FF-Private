package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.Strats
import asiankoala.ftc2021.commands.sequences.teleop.DepositSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.sequences.teleop.IntakeSequence
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.MecanumDriveCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger

open class HutaoTeleOp(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private val strategy = Strategy(alliance)

    override fun mInit() {
        hutao = Hutao(Pose(heading = 90.0.radians))
        hutao.encoders.odo.unregister()
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
                xScalar = 0.7, rScalar = 0.7
            )
        )
    }

    private fun bindDuck() {
        driver.dpadUp.onPress(InstantCommand({hutao.duck.setSpeed(0.3 * alliance.decide(1.0, -1.0))}, hutao.duck))
        driver.dpadRight.onPress(InstantCommand({hutao.duck.setSpeed(1.0 * alliance.decide(1.0, -1.0))}, hutao.duck))
        driver.dpadDown.onPress(InstantCommand({hutao.duck.setSpeed(0.0)}, hutao.duck))
    }

    private fun bindCycling() {
        driver.rightTrigger.onPress(IntakeSequence(::strategy, hutao.intake, hutao.outtake,
                hutao.indexer, hutao.turret, hutao.arm, hutao.slides))

        val depositCommand = SequentialCommandGroup(
            DepositSequence(::strategy, hutao.slides, hutao.indexer, driver.leftTrigger::isJustPressed),
            HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.intake)
        )
        CommandScheduler.scheduleWatchdog({
            if(strategy.isAlliance || strategy.isAttackingOtherCrater) {
                driver.leftTrigger.isJustPressed
            } else {
                true
            }  && !depositCommand.isScheduled }, depositCommand)
    }

    private fun bindStrategy() {
        driver.leftBumper.onPress(InstantCommand( { strategy.strat = Strats.ALLIANCE_BLUE }))
        driver.rightBumper.onPress(InstantCommand( { strategy.strat = Strats.SHARED_BLUE }))
        driver.x.onPress(InstantCommand( { strategy.strat = Strats.ALLIANCE_RED }))
        driver.b.onPress(InstantCommand( { strategy.strat = Strats.SHARED_RED }))
        driver.a.onPress(InstantCommand({strategy.shouldExtendFurther = false}))
        driver.y.onPress(InstantCommand({strategy.shouldExtendFurther= true}))
    }

    override fun mLoop() {
        strategy.log()
    }
}

