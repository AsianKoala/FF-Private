package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.Intake
import robotuprising.koawalib.command.commands.CommandBase
import robotuprising.koawalib.command.commands.InstantCommand

object IntakeCommands {
    class IntakeSmartCommand(private val intake: Intake) : CommandBase() {
        override fun init() {
            intake.startReading()
            intake.turnOn()
        }

        override fun execute() {

        }

        override fun end(interrupted: Boolean) {
            intake.stopReading()
            intake.turnOff()
        }

        override val isFinished: Boolean
            get() = intake.isMineralIn

        init {
            addRequirements(intake)
        }
    }

    class IntakeOnCommand(private val intake: Intake) : InstantCommand(intake::turnOn, intake)
    class IntakeOffCommand(private val intake: Intake) : InstantCommand(intake::turnOff, intake)
    class IntakeReverseCommand(private val intake: Intake) : InstantCommand(intake::turnReverse, intake)
}