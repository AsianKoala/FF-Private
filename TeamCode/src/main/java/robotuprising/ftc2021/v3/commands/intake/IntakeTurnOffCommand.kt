package robotuprising.ftc2021.v3.commands.intake

import robotuprising.ftc2021.v3.subsystems.Intake
import robotuprising.koawalib.command.commands.Command

class IntakeTurnOffCommand(private val intake: Intake) : Command {
    override fun execute() {
        intake.turnOff()
    }

    init {
        addRequirements(intake)
    }
}