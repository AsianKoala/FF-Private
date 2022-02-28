package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.Outtake
import robotuprising.koawalib.command.commands.InstantCommand

object OuttakeCommands {
    open class OuttakeCommand(outtake: Outtake, position: Double) : InstantCommand({ outtake.setPosition(position) }) {
        init {
            addRequirements(outtake)
        }
    }

    class Home(outtake: Outtake) : OuttakeCommand(outtake, Outtake.HOME_POSITION)
    class Transfer(outtake: Outtake) : OuttakeCommand(outtake, Outtake.TRANSFER_POSITION)
    class Deposit(outtake: Outtake) : OuttakeCommand(outtake, Outtake.DEPOSIT_POSITION)
}