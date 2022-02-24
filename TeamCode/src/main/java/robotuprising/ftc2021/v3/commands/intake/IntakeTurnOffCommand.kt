package robotuprising.ftc2021.v3.commands.intake

import robotuprising.ftc2021.v3.subsystems.Intake
import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.CommandBase

class IntakeTurnOffCommand(private val intake: Intake) : CommandBase() {
    override fun execute() {
        intake.turnOff()
    }

    init {
        addRequirements(intake)
    }
}