package robotuprising.koawalib.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import robotuprising.koawalib.command.CommandScheduler
import robotuprising.koawalib.command.commands.Command

class CommandGamepad(gamepad: Gamepad) : GamepadBase<CommandButton, CommandAxis>(gamepad, CommandButton::class.java, CommandAxis::class.java) {

    fun scheduleLeftStick(f: (Double, Double) -> Command) = scheduleStick(leftStick, f)

    fun scheduleRightStick(f: (Double, Double) -> Command) = scheduleStick(rightStick, f)

    fun scheduleDpad(f: (Double, Double) -> Command) = scheduleStick(dpad, f)

    fun scheduleStick(s: Stick, f: (Double, Double) -> Command): CommandGamepad {
        CommandScheduler.scheduleWatchdog({true}, f.invoke(s.getXAxis(), s.getYAxis()))
        return this
    }

    override fun enable(): CommandGamepad {
        return super.enable() as CommandGamepad
    }

    override fun disable(): CommandGamepad {
        return super.disable() as CommandGamepad
    }
}