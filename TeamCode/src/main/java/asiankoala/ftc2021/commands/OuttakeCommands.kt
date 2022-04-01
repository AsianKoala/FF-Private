package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.InstantCommand

object OuttakeCommands {
    class OuttakeHomeCommand(outtake: Outtake) : InstantCommand(outtake::home, outtake)
    class OuttakeCockCommand(outtake: Outtake) : InstantCommand(outtake::cock, outtake)
    class OuttakeCockMoreCommand(outtake: Outtake) : InstantCommand(outtake::cockMore, outtake)
    class OuttakeDepositHighCommand(outtake: Outtake) : InstantCommand(outtake::depositHigh, outtake)
    class OuttakeDepositSharedCommand(outtake: Outtake) : InstantCommand(outtake::depositShared, outtake)
}