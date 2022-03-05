package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.InstantCommand

object OuttakeCommands {
    open class OuttakeCommand(outtake: Outtake, position: Double) : InstantCommand({ outtake.setPosition(position) }) {
        init {
            addRequirements(outtake)
        }
    }

    class Home(outtake: Outtake) : OuttakeCommand(outtake, Outtake.HOME_POSITION)
    class Deposit(outtake: Outtake) : OuttakeCommand(outtake, Outtake.DEPOSIT_POSITION)
}
