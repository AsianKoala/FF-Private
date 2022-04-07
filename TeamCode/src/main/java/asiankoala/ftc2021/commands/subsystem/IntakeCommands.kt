package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Intake
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand

object IntakeCommands {
    class IntakeTurnOnCommand(intake: Intake) : InstantCommand({intake.setIntakeSpeed(0.85)}, intake)
    class IntakeTurnOffCommand(intake: Intake) : InstantCommand(intake::turnOff, intake)
    class IntakeTurnReverseCommand(intake: Intake) : InstantCommand(intake::turnReverse, intake)
    class IntakeStartReadingCommand(intake: Intake) : InstantCommand(intake::startReading, intake)
    class IntakeStopReadingCommand(intake: Intake) : InstantCommand(intake::stopReading, intake)
    class IntakeHasMineralCommand(intake: Intake) : WaitUntilCommand(intake::hasMineral)
}
