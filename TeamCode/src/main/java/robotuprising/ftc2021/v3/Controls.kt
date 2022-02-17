package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.intake.IntakeTurnOffCommand
import robotuprising.ftc2021.v3.commands.intake.IntakeTurnOnCommand
import robotuprising.ftc2021.v3.commands.intake.IntakeTurnReverseCommand
import robotuprising.koawalib.gamepad.CommandGamepad

class Controls (val robot: Robot, val driver: CommandGamepad, val gunner: CommandGamepad) {
    val intakeOnButton = driver.rightTrigger.getAsButton()
    val intakeOffButton = driver.leftTrigger.getAsButton()

    val intakeReverseButton = gunner.rightBumper

    fun bindControls() {
        bindIntakeControls()
    }

    fun bindIntakeControls() {
        intakeOnButton.whilePressedOnce(IntakeTurnOnCommand(robot.intakeSubsystem))
        intakeOffButton.whilePressedOnce(IntakeTurnOffCommand(robot.intakeSubsystem))
        intakeReverseButton.whilePressedOnce(IntakeTurnReverseCommand(robot.intakeSubsystem))
    }
}