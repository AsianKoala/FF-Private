package robotuprising.koawalib.command.commands

// useful to schedule other commands
class ScheduleCommand(vararg commands: Command) : CommandBase() {
    private val mToSchedule = HashSet<Command>(commands.toList())

    override fun execute() {
        mToSchedule.forEach(Command::schedule)
    }

    override val isFinished: Boolean = true
    override val runsWhenDisabled: Boolean = true
}