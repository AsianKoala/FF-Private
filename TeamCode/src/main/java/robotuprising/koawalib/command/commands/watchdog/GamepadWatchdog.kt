package robotuprising.koawalib.command.commands.watchdog

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.CommandBase
import robotuprising.koawalib.command.scheduler.CommandScheduler

class GamepadWatchdog(
        private val condition: () -> Boolean,
        private val toSchedule: Command
) : CommandBase() {

    override fun execute() {
        if(condition.invoke() && CommandScheduler.isOpModeLooping) {
            toSchedule.schedule()
        }
    }

    override val isFinished: Boolean = false
    override val runsWhenDisabled: Boolean = true
}