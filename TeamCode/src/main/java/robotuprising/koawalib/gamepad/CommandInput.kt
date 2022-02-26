package robotuprising.koawalib.gamepad

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.watchdog.GamepadWatchdog
import robotuprising.koawalib.util.interfaces.KBoolean


interface CommandInput<T : ButtonBase> : KBoolean {
    fun whenPressed(command: Command): T {
        return schedule(instance()::isJustPressed, command)
    }

    fun whenReleased(command: Command): T {
        return schedule(instance()::isJustReleased, command)
    }

    fun whilePressed(command: Command): T {
        return schedule(instance()::isPressed, command.cancelUpon(instance()::isReleased))
    }

    fun whileReleased(command: Command): T {
        return schedule(instance()::isReleased, command.cancelUpon(instance()::isPressed))
    }

    fun whilePressedOnce(command: Command): T {
        return schedule(instance()::isJustPressed, command.cancelUpon(instance()::isReleased))
    }

    fun whilePressedContinuous(command: Command): T {
        return schedule(instance()::isPressed, command)
    }

    fun whileReleasedOnce(command: Command): T {
        return schedule(instance()::isJustReleased, command.cancelUpon(instance()::isPressed))
    }

    fun whenToggled(command: Command): T {
        return schedule(instance()::isJustToggled, command)
    }

    fun whenInverseToggled(command: Command): T {
        return schedule(instance()::isJustInverseToggled, command)
    }

    fun whileToggled(command: Command): T {
        return schedule(instance()::isToggled, command.cancelUpon(instance()::isInverseToggled))
    }

    fun whileInverseToggled(command: Command): T {
        return schedule(instance()::isInverseToggled, command.cancelUpon(instance()::isToggled))
    }

    /** Return instance of class parameter
     *
     * @return The instance
     */
    fun instance(): T

    /** Schedule the commands
     *
     * @param condition The condition
     * @param command The command to schedule
     * @return The instance
     */
    fun schedule(condition: () -> Boolean, command: Command): T {
        GamepadWatchdog(condition, command).schedule()
        return instance()
    }

    /** just schedules things
     *
     * @param command command
     * @return the instance
     */
    fun schedule(command: Command): T {
        return schedule({ true }, command)
    }

    fun whenPressedReleased(press: Command, release: Command): T {
        whenPressed(press)
        return whenReleased(release)
    }

    fun whilePressedReleased(press: Command, release: Command): T {
        whilePressed(press)
        return whileReleased(release)
    }

    fun toggle(toggle: Command, itoggle: Command): T {
        whenToggled(toggle)
        return whenInverseToggled(itoggle)
    }
}
