package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Outtake
import com.asiankoala.koawalib.command.commands.InstantCommand

object OuttakeCommands {
    class OuttakeHomeCommand(outtake: Outtake) : InstantCommand(outtake::home, outtake)
    class OuttakeCockCommand(outtake: Outtake) : InstantCommand(outtake::cock, outtake)
    class OuttakeDepositHighCommand(outtake: Outtake) : InstantCommand(outtake::depositHigh, outtake)
    class OuttakeDepositSharedCommand(outtake: Outtake) : InstantCommand(outtake::depositShared, outtake)
}