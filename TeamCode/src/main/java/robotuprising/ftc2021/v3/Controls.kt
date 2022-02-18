package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.intake.IntakeSmartCommand
import robotuprising.koawalib.gamepad.CommandGamepad

class Controls (private val robot: Robot, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    val intakeButton = driver.rightTrigger.getAsButton()

    fun bindControls() {
        intakeButton.whenPressed(IntakeSmartCommand(robot.intakeSubsystem))
    }
}