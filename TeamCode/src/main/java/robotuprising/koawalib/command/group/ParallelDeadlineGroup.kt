package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.scheduler.CommandScheduler

class ParallelDeadlineGroup(private val deadline: Command, vararg commands: Command) : CommandGroup(true, *commands) {
    override fun schedule(c: Command) {
        CommandScheduler.scheduleWithOther(this, c)
    }

    override fun isFinished(): Boolean {
        return deadline.justFinished
    }

    init {
        addCommands(deadline)
    }
}