package robotuprising.ftc2021.v3.commands.intake

import robotuprising.ftc2021.v3.subsystems.Intake

class IntakeSmartCommand(private val intake: Intake) : IntakeTurnOnCommand(intake) {

    override val isFinished: Boolean
        get() = intake.isMineralIn

    override fun end(cancel: Boolean) {
        intake.turnOff()
    }
}