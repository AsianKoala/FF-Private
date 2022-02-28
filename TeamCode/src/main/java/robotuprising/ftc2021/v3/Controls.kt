package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.*
import robotuprising.koawalib.command.CommandScheduler
import robotuprising.koawalib.command.commands.InstantCommand
import robotuprising.koawalib.command.commands.WaitCommand
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.subsystem.drive.MecanumDriveCommand

class Controls (private val otonashi: Otonashi, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    fun bindControls() {
        otonashi.drive.setDefaultCommand(MecanumDriveCommand(otonashi.drive, driver.leftStick, driver.rightStick))

        driver.rightTrigger.whenPressed(
                IntakeCommands.IntakeSmartCommand(otonashi.intake)
                        .andThen(
                                IndexerCommands.Lock(otonashi.indexer)
                                        .alongWith(OuttakeCommands.Transfer(otonashi.outtake))
                        )
        )

        CommandScheduler.scheduleWatchdog({ driver.leftTrigger.isJustPressed && otonashi.hubState == HubState.ALLIANCE_HIGH && otonashi.intake.isMineralIn },
                        TurretCommands.AllianceCommand(otonashi.turret)
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
        )

        gunner.dpadUp.whenPressed(InstantCommand(otonashi::incrementState))
        gunner.dpadDown.whenPressed(InstantCommand(otonashi::decrementState))
    }
}
