package robotuprising.koawalib.gamepad

import com.qualcomm.robotcore.hardware.Gamepad
import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.scheduler.CommandScheduler

class CommandGamepad(gamepad: Gamepad) : GamepadBase<CommandButton, CommandAxis>(gamepad, CommandButton::class.java, CommandAxis::class.java) {

    fun scheduleLeftStick(f: (Double, Double) -> Command) = scheduleStick(leftStick, f)
//    @JvmName("bruh3")
//    fun scheduleLeftStick(f: (Double, Double) -> Unit) = scheduleStick(leftStick, f)

    fun scheduleRightStick(f: (Double, Double) -> Command) = scheduleStick(rightStick, f)
//    @JvmName("bruh4")
//    fun scheduleRightStick(f: (Double, Double) -> Unit) = scheduleStick(rightStick, f)

    fun scheduleDpad(f: (Double, Double) -> Command) = scheduleStick(dpad, f)
//    @JvmName("bruh5")
//    fun scheduleDpad(f: (Double, Double) -> Unit) = scheduleStick(dpad, f)

    fun scheduleStick(s: Stick, f: (Double, Double) -> Command): CommandGamepad {
        CommandScheduler.scheduleJoystick(f.invoke(s.getXAxis(), s.getYAxis()))
        return this
    }

//    @JvmName("bruh2")
//    fun scheduleStick(s: Stick, f: (Double, Double) -> Unit): CommandGamepad {
//        CommandScheduler.scheduleJoystick { f.invoke(s.getXAxis(), s.getYAxis()) }
//        return this
//    }

    override fun enable(): CommandGamepad {
        return super.enable() as CommandGamepad
    }

    override fun disable(): CommandGamepad {
        return super.disable() as CommandGamepad
    }
}