package robotuprising.koawalib.command.commands

import robotuprising.koawalib.subsystem.Subsystem

open class InstantCommand(
        private val action: () -> Unit = {},
        vararg requirements: Subsystem
): CommandBase() {

    override fun execute() {
        action.invoke()
    }

    init {
        addRequirements(*requirements)
    }
}