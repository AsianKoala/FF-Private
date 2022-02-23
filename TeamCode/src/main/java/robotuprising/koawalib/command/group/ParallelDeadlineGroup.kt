package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command
import java.util.*
import kotlin.collections.HashMap

class ParallelDeadlineGroup(private var mDeadline: Command, vararg commands: Command) : CommandGroupBase() {
    // maps commands in this group to whether they are still running
    private val mCommands: MutableMap<Command, Boolean> = HashMap()
    private var mRunWhenDisabled = true

    /**
     * Sets the deadline to the given command.  The deadline is added to the group if it is not
     * already contained.
     *
     * @param deadline the command that determines when the group ends
     */
    fun setDeadline(deadline: Command) {
        if (!mCommands.containsKey(deadline)) {
            addCommands(deadline)
        }
        mDeadline = deadline
    }

    override fun addCommands(vararg commands: Command) {
        requireUngrouped(*commands)
        check(!mCommands.containsValue(true)) { "Commands cannot be added to a CommandGroup while the group is running" }
        registerGroupedCommands(*commands)
        for (command in commands) {
            require(Collections.disjoint(command.getRequirements(), mRequirements)) {
                ("Multiple commands in a parallel group cannot"
                        + "require the same subsystems")
            }
            mCommands[command] = false
            mRequirements.addAll(command.getRequirements())
            mRunWhenDisabled = mRunWhenDisabled and command.runsWhenDisabled
        }
    }

    fun initialize() {
        for (commandRunning in mCommands.entries) {
            commandRunning.key.init()
            commandRunning.setValue(true)
        }
    }

    override fun execute() {
        for (commandRunning in mCommands.entries) {
            if (!commandRunning.value) {
                continue
            }
            commandRunning.key.execute()
            if (commandRunning.key.isFinished) {
                commandRunning.key.end(false)
                commandRunning.setValue(false)
            }
        }
    }

    override fun end(interrupted: Boolean) {
        for ((key, value) in mCommands) {
            if (value) {
                key.end(true)
            }
        }
    }

    override val isFinished: Boolean
        get() = mDeadline.isFinished

    override val runsWhenDisabled: Boolean get() = mRunWhenDisabled

    /**
     * Creates a new ParallelDeadlineGroup.  The given commands (including the deadline) will be
     * executed simultaneously.  The CommandGroup will finish when the deadline finishes,
     * interrupting all other still-running commands.  If the CommandGroup is interrupted, only
     * the commands still running will be interrupted.
     *
     * @param deadline the command that determines when the group ends
     * @param commands the commands to be executed
     */
    init {
        addCommands(*commands)
        if (!mCommands.containsKey(mDeadline)) {
            addCommands(mDeadline)
        }
    }
}
