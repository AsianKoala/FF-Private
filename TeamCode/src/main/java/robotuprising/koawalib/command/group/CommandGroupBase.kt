package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.CommandBase
import java.util.*

abstract class CommandGroupBase : CommandBase(), Command {
    companion object {
        private val groupCommands: MutableSet<Command> = Collections.newSetFromMap(WeakHashMap())

        fun registerGroupedCommands(vararg commands: Command) {
            groupCommands.addAll(commands)
        }

        fun clearGroupedCommands() {
            groupCommands.clear()
        }

        fun clearGroupCommand(c: Command) {
            groupCommands.remove(c)
        }

        fun requireUngrouped(vararg commands: Command) {
            requireUngrouped(*commands)
        }

        fun requireUngrouped(commands: Collection<Command>) {
            if(!Collections.disjoint(groupCommands, commands)) {
                throw IllegalArgumentException("Commands cannot be added to more than one CommandGroup");
            }
        }

        fun getGroupedCommands(): Set<Command> {
            return groupCommands
        }
    }

    abstract fun addCommands(vararg commands: Command)
}