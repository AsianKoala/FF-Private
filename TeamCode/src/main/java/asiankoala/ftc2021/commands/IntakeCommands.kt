package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Intake
import com.asiankoala.koawalib.command.commands.InstantCommand

object IntakeCommands {
    class IntakeTurnOnCommand(intake: Intake) : InstantCommand(intake::turnOn, intake)
    class IntakeTurnOffCommand(intake: Intake) : InstantCommand(intake::turnOff, intake)
    class IntakeTurnReverseCommand(intake: Intake) : InstantCommand(intake::turnReverse, intake)
}