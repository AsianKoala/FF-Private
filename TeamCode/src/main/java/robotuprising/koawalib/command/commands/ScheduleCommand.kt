package robotuprising.koawalib.command.commands

class ScheduleCommand(vararg commands: Command) : CommandBase() {
    private val mToSchedule = HashSet<Command>(commands.toList())

    override fun execute() {
        mToSchedule.forEach(Command::schedule)
    }

    override val isFinished: Boolean = true
    override val runsWhenDisabled: Boolean = true
}