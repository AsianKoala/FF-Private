package robotuprising.koawalib.command.commands

import robotuprising.koawalib.subsystem.Subsystem

fun interface Command {
    fun init() {}

    fun execute()

    fun end(interrupted: Boolean) {}

    val isFinished: Boolean get() = false

    fun getRequirements(): Set<Subsystem> { return HashSet() }

    fun hasRequirement(requirement: Subsystem): Boolean {
        return getRequirements().contains(requirement)
    }

    fun schedule(interruptible: Boolean) {
        TODO()
    }

    fun schedule() {
        schedule(true)
    }

    fun cancel() {
        TODO()
    }

    val isScheduled: Boolean get() = TODO()

    val runsWhenDisabled: Boolean get() = TODO()

    val name: String get() = this.javaClass.simpleName
}