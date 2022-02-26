package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.commands.intake.IntakeSmartCommand
import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.util.Alliance
import robotuprising.koawalib.subsystem.drive.MecanumDriveCommand

class Controls (private val rin: Rin, private val driver: CommandGamepad, private val gunner: CommandGamepad) {
    private val intakeButton = driver.rightTrigger.getAsButton()

    fun bindControls() {
        intakeButton.whenPressed(IntakeSmartCommand(rin.intake))

        rin.kei.setDefaultCommand(MecanumDriveCommand(
                rin.kei, driver.leftStick, driver.rightStick, Alliance.BLUE,
                0.5, 0.5, 0.5, fieldOriented = true,
                headingLock = true, { rin.kei.position.heading }, 180.0))


        driver.scheduleLeftStick { left, right -> Command { println(left + right) } }
    }
}
