package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.intake.IntakeSmartCommand
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.structure.Alliance
import robotuprising.koawalib.subsystem.drive.MecanumDriveManualCommand

class Controls (private val rin: Rin, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    private val intakeButton = driver.rightTrigger.getAsButton()

    fun bindControls() {
        intakeButton.whenPressed(IntakeSmartCommand(rin.intakeSubsystem))

        rin.kei.setDefaultCommand(MecanumDriveManualCommand(
                rin.kei, driver.leftStick, driver.rightStick, Alliance.BLUE,
                0.5, 0.5, 0.5, fieldOriented = true,
                headingLock = true, { rin.kei.position.h }, 180.0))
    }
}
