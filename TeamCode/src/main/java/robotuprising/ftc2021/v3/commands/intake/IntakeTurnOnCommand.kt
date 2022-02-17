package robotuprising.ftc2021.v3.commands.intake

import robotuprising.ftc2021.v3.subsystems.IntakeSubsystem
import robotuprising.koawalib.command.commands.Command

class IntakeTurnOnCommand(private val intakeSubsystem: IntakeSubsystem): Command {
    override fun execute() {
        intakeSubsystem.turnOn()
    }

    init {
        addRequirements(intakeSubsystem)
    }
}