package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.scheduler.CommandScheduler

class ParallelCommandGroup(vararg commands: Command) : CommandGroup(true, *commands) {
    override fun schedule(c: Command) {
        CommandScheduler.scheduleWithOther(this, c)
    }

    override fun isFinished(): Boolean {
        // if there are no unfinished commands its done
        return !commandMap.containsValue(false)
    }
}