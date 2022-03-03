package asiankoala.ftc21.v3.opmodes

import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.CommandScheduler
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.subsystem.drive.MecanumDriveCommand
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import asiankoala.ftc21.v3.Hardware
import asiankoala.ftc21.v3.HubState
import asiankoala.ftc21.v3.Otonashi
import asiankoala.ftc21.v3.commands.DepositCommands
import asiankoala.ftc21.v3.commands.IntakeCommands

@TeleOp
class TeleOp : CommandOpMode() {
    private val otonashi by lazy { Otonashi(Hardware()) }
    override fun mInit() {
        bindDrive()
        bindIntake()
        bindDeposit()
        bindStateChange()
    }

    private fun bindDrive() {
        otonashi.drive.setDefaultCommand(MecanumDriveCommand(otonashi.drive, driver.leftStick, driver.rightStick))
    }

    private fun bindIntake() {
        driver.rightTrigger.whenPressed(IntakeCommands.IntakeSequenceCommand(otonashi.intake, otonashi.indexer, otonashi.outtake))
    }

    private fun bindDeposit() {
        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_HIGH && otonashi.intake.isMineralIn },
                DepositCommands.DepositHigh(
                        driver.leftTrigger::isJustPressed, otonashi.intake, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))

        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_MID && otonashi.intake.isMineralIn },
                DepositCommands.DepositMid(
                        driver.leftTrigger::isJustPressed, otonashi.intake, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))

        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_LOW && otonashi.intake.isMineralIn },
                DepositCommands.DepositLow(
                        driver.leftTrigger::isJustPressed, otonashi.intake, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))
    }

    private fun bindStateChange() {
        driver.leftBumper.whenPressed(InstantCommand(otonashi::decrementState))
        driver.rightBumper.whenPressed(InstantCommand(otonashi::incrementState))
    }
}