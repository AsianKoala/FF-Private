package asiankoala.ftc21.v3.commands

import com.asiankoala.koawalib.command.commands.InstantCommand
import asiankoala.ftc21.v3.subsystems.Outtake

object OuttakeCommands {
    open class OuttakeCommand(outtake: Outtake, position: Double) : InstantCommand({ outtake.setPosition(position) }) {
        init {
            addRequirements(outtake)
        }
    }

    class Home(outtake: Outtake) : OuttakeCommand(outtake, Outtake.HOME_POSITION)
    class Deposit(outtake: Outtake) : OuttakeCommand(outtake, Outtake.DEPOSIT_POSITION)
}