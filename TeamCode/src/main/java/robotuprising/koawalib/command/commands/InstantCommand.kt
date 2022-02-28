package robotuprising.koawalib.command.commands

import robotuprising.koawalib.subsystem.Subsystem

// instant command: ends immediately after running
open class InstantCommand(
        private val action: () -> Unit,
        vararg requirements: Subsystem
): CommandBase() {

    override fun execute() {
        action.invoke()
    }

    override val isFinished: Boolean = true

    init {
        addRequirements(*requirements)
    }
}