package robotuprising.koawalib.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.watchdog.StickWatchdog

class CommandGamepad(gamepad: Gamepad) : GamepadBase<CommandButton, CommandAxis>(gamepad, CommandButton::class.java, CommandAxis::class.java) {

    fun scheduleLeftStick(f: (Double, Double) -> Command) = scheduleStick(leftStick, f)

    fun scheduleRightStick(f: (Double, Double) -> Command) = scheduleStick(rightStick, f)

    fun scheduleDpad(f: (Double, Double) -> Command) = scheduleStick(dpad, f)

    fun scheduleStick(s: Stick, f: (Double, Double) -> Command): CommandGamepad {
        StickWatchdog(f.invoke(s.getXAxis(), s.getYAxis())).schedule()
        return this
    }

    override fun enable(): CommandGamepad {
        return super.enable() as CommandGamepad
    }

    override fun disable(): CommandGamepad {
        return super.disable() as CommandGamepad
    }
}