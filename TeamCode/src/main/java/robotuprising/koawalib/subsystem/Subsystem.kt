package robotuprising.koawalib.subsystem

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.scheduler.CommandScheduler
import robotuprising.koawalib.util.Periodic

interface Subsystem : Periodic {
    fun register() {
        CommandScheduler.register(this)
    }

    fun setDefaultCommand(c: Command): Subsystem {
       CommandScheduler.scheduleDefault(c, this)
       return this
    }

    fun getDefaultCommand(c: Command): Command {
        return CommandScheduler.getDefault(this)!!
    }

    override fun periodic() {

    }
}