package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.subsystem.IntakeCommands
import asiankoala.ftc2021.subsystems.Intake
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class AutoIntakeSequence(intake: Intake) : SequentialCommandGroup(
        IntakeCommands.IntakeStartReadingCommand(intake),
        InstantCommand(intake::turnOn),
        IntakeCommands.IntakeHasMineralCommand(intake),
        InstantCommand(intake::stopReading)
)