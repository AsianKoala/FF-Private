package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.*
import robotuprising.koawalib.command.CommandScheduler
import robotuprising.koawalib.command.commands.InstantCommand
import robotuprising.koawalib.command.commands.WaitCommand
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.subsystem.drive.MecanumDriveCommand

class Controls (private val otonashi: Otonashi, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    fun bindControls() {
        val driveCommand = MecanumDriveCommand(otonashi.drive, driver.leftStick, driver.rightStick)

        val intakeSequence = IntakeCommands.IntakeSmartCommand(otonashi.intake)
                .andThen(
                        IndexerCommands.Lock(otonashi.indexer)
                                .alongWith(OuttakeCommands.Transfer(otonashi.outtake))
                )

        val depositSequence = TurretCommands.AllianceCommand(otonashi.turret)
                .alongWith(
                        PitchCommands.AllianceHighCommand(otonashi.pitch),
                        SlideCommands.AllianceHighCommand(otonashi.slides),
                        WaitCommand(0.7).andThen(OuttakeCommands.Deposit(otonashi.outtake))
                )

                .continueIf(driver.rightBumper::isPressed)

                .andThen(
                        IndexerCommands.Index(otonashi.indexer).sleep(0.5),
                        IndexerCommands.Home(otonashi.indexer),
                        OuttakeCommands.Home(otonashi.outtake),
                        TurretCommands.HomeCommand(otonashi.turret)
                                .alongWith(
                                        PitchCommands.HomeCommand(otonashi.pitch),
                                        SlideCommands.HomeCommand(otonashi.slides)
                                )
                )

        otonashi.drive.setDefaultCommand(driveCommand)
        driver.rightTrigger.whenPressed(intakeSequence)

        CommandScheduler.scheduleWatchdog({
            driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_HIGH && otonashi.intake.isMineralIn }, depositSequence)

        gunner.dpadUp.whenPressed(InstantCommand(otonashi::incrementState))
        gunner.dpadDown.whenPressed(InstantCommand(otonashi::decrementState))
    }
}
