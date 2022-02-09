package robotuprising.koawalib.command.commands

import robotuprising.koawalib.subsystem.Subsystem
import robotuprising.koawalib.util.Clearable
import java.util.function.Supplier

interface Command : Supplier<Command> {
    companion object : Clearable {
        private val stateMap: MutableMap<Command, CommandState> = HashMap()
        private val timeMap: MutableMap<Command, CommandState> = HashMap()
        private val requirementMap: MutableMap<Command, Set<Subsystem>> = HashMap()

        override fun clear() {
            stateMap.clear()
            timeMap.clear()
            requirementMap.clear()
        }
    }

    fun addRequirements(vararg requirements: Subsystem): Set<Subsystem> {
        requirementMap.putIfAbsent(this, LinkedHashSet())
        return requirementMap[this]!!
    }

    fun init() {

    }

    fun execute()

    val isFinished get() = true

    fun end(cancel: Boolean) {

    }


}