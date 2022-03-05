package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hub
import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.DepositCommands
import asiankoala.ftc2021.commands.IntakeCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.subsystem.drive.MecanumDriveCommand
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class HutaoTeleOp : CommandOpMode() {
    private val hutao by lazy { Hutao() }
    override fun mInit() {
        bindDrive()
        bindIntake()
        bindDeposit()
        bindStateChange()
    }

    private fun bindDrive() {
        hutao.drive.setDefaultCommand(MecanumDriveCommand(hutao.drive, driver.leftStick, driver.rightStick))
    }

    private fun bindIntake() {
        driver.rightTrigger.whenPressed(IntakeCommands.IntakeSequenceCommand(hutao.intake, hutao.indexer, hutao.outtake))
    }

    private fun bindDeposit() {
        CommandScheduler.scheduleWatchdog(
            {
                driver.leftTrigger.isJustPressed && hutao.hub == Hub.ALLIANCE_HIGH && hutao.intake.isMineralIn
            },
            DepositCommands.DepositHigh(
                driver.leftTrigger::isJustPressed, hutao.intake, hutao.turret, hutao.pitch, hutao.slides, hutao.outtake, hutao.indexer
            )
        )

        CommandScheduler.scheduleWatchdog(
            {
                driver.leftTrigger.isJustPressed && hutao.hub == Hub.ALLIANCE_MID && hutao.intake.isMineralIn
            },
            DepositCommands.DepositMid(
                driver.leftTrigger::isJustPressed, hutao.intake, hutao.turret, hutao.pitch, hutao.slides, hutao.outtake, hutao.indexer
            )
        )

        CommandScheduler.scheduleWatchdog(
            {
                driver.leftTrigger.isJustPressed && hutao.hub == Hub.ALLIANCE_LOW && hutao.intake.isMineralIn
            },
            DepositCommands.DepositLow(
                driver.leftTrigger::isJustPressed, hutao.intake, hutao.turret, hutao.pitch, hutao.slides, hutao.outtake, hutao.indexer
            )
        )
    }

    private fun bindStateChange() {
        driver.leftBumper.whenPressed(InstantCommand(hutao::decHub))
        driver.rightBumper.whenPressed(InstantCommand(hutao::incHub))
    }
}
