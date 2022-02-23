package robotuprising.koawalib.command.group

import robotuprising.koawalib.command.commands.Command

class SequentialCommandGroup(vararg commands: Command) : CommandGroupBase() {
    private val mCommands: MutableList<Command> = ArrayList()
    private var mCurrentCommandIndex = -1
    private var mRunWhenDisabled = true
    override fun addCommands(vararg commands: Command) {
        requireUngrouped(commands)
        check(mCurrentCommandIndex == -1) { "Commands cannot be added to a CommandGroup while the group is running" }
        registerGroupedCommands(commands)
        for (command in commands) {
            mCommands.add(command)
            m_requirements.addAll(command.getRequirements())
            mRunWhenDisabled = mRunWhenDisabled and command.runsWhenDisabled()
        }
    }

    fun initialize() {
        mCurrentCommandIndex = 0
        if (!mCommands.isEmpty()) {
            mCommands[0].initialize()
        }
    }

    override fun execute() {
        if (mCommands.isEmpty()) {
            return
        }
        val currentCommand = mCommands[mCurrentCommandIndex]
        currentCommand.execute()
        if (currentCommand.isFinished()) {
            currentCommand.end(false)
            mCurrentCommandIndex++
            if (mCurrentCommandIndex < mCommands.size) {
                mCommands[mCurrentCommandIndex].initialize()
            }
        }
    }

    override fun end(interrupted: Boolean) {
        if (interrupted && !mCommands.isEmpty()) {
            mCommands[mCurrentCommandIndex].end(true)
        }
        mCurrentCommandIndex = -1
    }

    override val isFinished: Boolean
        get() = mCurrentCommandIndex == mCommands.size

    fun runsWhenDisabled(): Boolean {
        return mRunWhenDisabled
    }

    /**
     * Creates a new SequentialCommandGroup.  The given commands will be run sequentially, with
     * the CommandGroup finishing when the last command finishes.
     *
     * @param commands the commands to include in this group.
     */
    init {
        addCommands(*commands)
    }
}
