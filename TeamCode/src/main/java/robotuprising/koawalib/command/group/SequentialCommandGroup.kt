package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.scheduler.CommandScheduler

class SequentialCommandGroup(vararg c: Command) : CommandGroup(true, *c) {
    protected var lastCommand: Command? = null

    override fun schedule(c: Command) {
        if(lastCommand == null) {
            CommandScheduler.scheduleWithOther(this, c)
        } else {
            CommandScheduler.scheduleAfterOther(lastCommand!!, c)
        }

        lastCommand = c
    }

    override fun isFinished() = lastCommand!!.isFinished() || (anyCancelled && !countCancel)
}