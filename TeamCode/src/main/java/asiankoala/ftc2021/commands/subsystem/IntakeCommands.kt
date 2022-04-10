package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Intake
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand

object IntakeCommands {
    class IntakeTurnOnCommand(intake: Intake) : InstantCommand(intake::turnOn)
    class IntakeTurnOffCommand(intake: Intake) : InstantCommand(intake::turnOff)
    class IntakeTurnReverseCommand(intake: Intake) : InstantCommand(intake::turnReverse)
    class IntakeStartReadingCommand(intake: Intake) : InstantCommand(intake::startReading)
    class IntakeStopReadingCommand(intake: Intake) : InstantCommand(intake::stopReading)
    class IntakeHasMineralCommand(intake: Intake) : WaitUntilCommand(intake::hasMineral)
}
