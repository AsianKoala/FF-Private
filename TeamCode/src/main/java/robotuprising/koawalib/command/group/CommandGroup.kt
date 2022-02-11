package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command

abstract class CommandGroup(protected var countCancel: Boolean = false, vararg commands: Command) : Command {
    protected val commandMap = HashMap<Command, Boolean>()
    protected var anyCancelled = false

    fun addCommands(vararg commands: Command): CommandGroup {
        commands.forEach {
            schedule(it)
            commandMap[it] = false
        }
        return this
    }

    fun countCancel(): CommandGroup {
        countCancel = true
        return this
    }

    fun ignoreCancel(): CommandGroup {
        countCancel = false
        return this
    }

    abstract fun schedule(c: Command)

    override fun initialize() {
        commandMap.replaceAll { _, _ -> false }
        anyCancelled = false
    }

    override fun execute() {
        commandMap.replaceAll { command, bool ->
            if(countCancel) {
                command.justFinished
            } else {
                command.justFinishedNoCancel || bool
            }
        }

        anyCancelled = commandMap.keys.stream().anyMatch(Command::isCancelled) || anyCancelled
    }

    abstract override fun isFinished(): Boolean

    override fun end(cancel: Boolean) {
        commandMap.keys.forEach(Command::cancel)
    }

    init {
        addCommands(*commands)
    }
}