package neil.ftc21.v3.commands

import neil.ftc21.v3.subsystems.Outtake
import neil.koawalib.command.commands.InstantCommand

object OuttakeCommands {
    open class OuttakeCommand(outtake: Outtake, position: Double) : InstantCommand({ outtake.setPosition(position) }) {
        init {
            addRequirements(outtake)
        }
    }

    class Home(outtake: Outtake) : OuttakeCommand(outtake, Outtake.HOME_POSITION)
    class Deposit(outtake: Outtake) : OuttakeCommand(outtake, Outtake.DEPOSIT_POSITION)
}