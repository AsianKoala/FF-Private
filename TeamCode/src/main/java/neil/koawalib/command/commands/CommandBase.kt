package neil.koawalib.command.commands

import neil.koawalib.subsystem.Subsystem

abstract class CommandBase : Command {
    protected val mRequirements: MutableSet<Subsystem> = HashSet()

    fun addRequirements(vararg subsystems: Subsystem) {
        mRequirements.addAll(subsystems)
    }

    override fun getRequirements(): Set<Subsystem> {
        return mRequirements
    }
}