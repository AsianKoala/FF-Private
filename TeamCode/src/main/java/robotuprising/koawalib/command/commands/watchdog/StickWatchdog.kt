package robotuprising.koawalib.command.commands.watchdog

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.CommandBase
import robotuprising.koawalib.command.scheduler.CommandScheduler

class StickWatchdog(
        private val command: Command
) : CommandBase() {

    override fun execute() {
        if(CommandScheduler.isOpModeLooping) {
            command.schedule()
        }
    }

    override val isFinished = false
    override val runsWhenDisabled = true
}