package robotuprising.koawalib.command.commands

import robotuprising.koawalib.command.group.CommandGroupBase


/**
 * Runs one of two commands, depending on the value of the given condition when this command is
 * initialized. Does not actually schedule the selected command - rather, the command is run
 * through this command; this ensures that the command will behave as expected if used as part of a
 * CommandGroup. Requires the requirements of both commands, again to ensure proper functioning
 * when used in a CommandGroup. If this is undesired, consider using [ScheduleCommand].
 *
 *
 * As this command contains multiple component commands within it, it is technically a command
 * group; the command instances that are passed to it cannot be added to any other groups, or
 * scheduled individually.
 *
 *
 * As a rule, CommandGroups require the union of the requirements of their component commands.
 *
 * @author Jackson
 */
class ConditionalCommand(onTrue: Command, onFalse: Command, condition: () -> Boolean) : CommandBase() {
    private val mOnTrue: Command
    private val mOnFalse: Command
    private val mCondition: () -> Boolean
    private lateinit var m_selectedCommand: Command

    override fun init() {
        m_selectedCommand = if (mCondition.invoke()) {
            mOnTrue
        } else {
            mOnFalse
        }

        m_selectedCommand.init()
    }

    override fun execute() {
        m_selectedCommand.execute()
    }

    override fun end(interrupted: Boolean) {
        m_selectedCommand.end(interrupted)
    }

    override val isFinished: Boolean
        get() = m_selectedCommand.isFinished

    override val runsWhenDisabled: Boolean get() = mOnTrue.runsWhenDisabled && mOnFalse.runsWhenDisabled

    /**
     * Creates a new ConditionalCommand.
     *
     * @param onTrue    the command to run if the condition is true
     * @param onFalse   the command to run if the condition is false
     * @param condition the condition to determine which command to run
     */
    init {
        CommandGroupBase.requireUngrouped(onTrue, onFalse)
        CommandGroupBase.registerGroupedCommands(onTrue, onFalse)
        mOnTrue = onTrue
        mOnFalse = onFalse
        mCondition = condition
        mRequirements.addAll(mOnTrue.getRequirements())
        mRequirements.addAll(mOnFalse.getRequirements())
    }
}
