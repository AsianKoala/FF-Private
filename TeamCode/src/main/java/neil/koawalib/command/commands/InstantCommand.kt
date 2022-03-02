package neil.koawalib.command.commands

import neil.koawalib.subsystem.Subsystem

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