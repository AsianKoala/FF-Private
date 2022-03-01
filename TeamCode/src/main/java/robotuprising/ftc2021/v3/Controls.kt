package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.*
import robotuprising.koawalib.command.CommandScheduler
import robotuprising.koawalib.command.commands.InstantCommand
import robotuprising.koawalib.command.commands.WaitCommand
import robotuprising.koawalib.command.commands.WaitUntilCommand
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.subsystem.drive.MecanumDriveCommand

class Controls (private val otonashi: Otonashi, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    fun bindControls() {
        bindDrive()
        bindIntake()
        bindDeposit()
        bindGunner()
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
                DepositCommands.DepositHigh(driver.rightBumper::isPressed, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))

        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_MID && otonashi.intake.isMineralIn },
                DepositCommands.DepositMid(driver.rightBumper::isPressed, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))

        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_LOW && otonashi.intake.isMineralIn },
                DepositCommands.DepositLow(driver.rightBumper::isPressed, otonashi.turret, otonashi.pitch, otonashi.slides, otonashi.outtake, otonashi.indexer))
    }

    private fun bindGunner() {
        gunner.dpadUp.whenPressed(InstantCommand(otonashi::incrementState))
        gunner.dpadDown.whenPressed(InstantCommand(otonashi::decrementState))
    }
}
