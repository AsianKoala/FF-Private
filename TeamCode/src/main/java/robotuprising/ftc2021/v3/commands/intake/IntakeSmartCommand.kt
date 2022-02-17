package robotuprising.ftc2021.v3.commands.intake

import robotuprising.ftc2021.v3.subsystems.IntakeSubsystem

class IntakeSmartCommand(private val intakeSubsystem: IntakeSubsystem) : IntakeTurnOnCommand(intakeSubsystem) {

    override fun isFinished(): Boolean {
        return intakeSubsystem.isMineralIn
    }

    override fun end(cancel: Boolean) {
        intakeSubsystem.turnOff()
    }
}