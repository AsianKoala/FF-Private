package robotuprising.koawalib.command.commands

class WaitUntilCommand(private val condition: () -> Boolean) : CommandBase() {
    override fun execute() {
        TODO("Not yet implemented")
    }

    override val runsWhenDisabled: Boolean = true
    override val isFinished: Boolean get() = condition.invoke()
}
