package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.intake.IntakeSmartCommand
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.structure.KControls

class Controls (private val robot: Robot) : KControls() {
    val intakeButton = driver.rightTrigger.getAsButton()

    override fun bindControls(driver: CommandGamepad, gunner: CommandGamepad) {
        super.bindControls(driver, gunner)

        intakeButton.whenPressed(IntakeSmartCommand(robot.intakeSubsystem))
    }
}