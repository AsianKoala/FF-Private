package robotuprising.koawalib.command

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.commands.CommandState
import robotuprising.koawalib.command.commands.InfiniteCommand
import robotuprising.koawalib.command.commands.Watchdog
import robotuprising.koawalib.command.group.CommandGroupBase
import robotuprising.koawalib.subsystem.Subsystem
import java.util.Collections
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object CommandScheduler {
    val mScheduledCommands: MutableMap<Command, CommandState> = LinkedHashMap()
    private val mRequirements: MutableMap<Subsystem, Command> = LinkedHashMap()
    private val mSubsystems: MutableMap<Subsystem, Command?> = LinkedHashMap()

    private val mInitActions: MutableList<(Command) -> Unit> = ArrayList()
    private val mExecuteActions: MutableList<(Command) -> Unit> = ArrayList()
    private val mInterruptActions: MutableList<(Command) -> Unit> = ArrayList()
    private val mFinishActions: MutableList<(Command) -> Unit> = ArrayList()

    private val mToSchedule: MutableMap<Command, Boolean> = LinkedHashMap()
    private val mToCancel: MutableList<Command> = ArrayList()

    private val allMaps = listOf(mScheduledCommands, mRequirements, mSubsystems, mToSchedule)
    private val allLists = listOf(mInitActions, mExecuteActions, mInterruptActions, mFinishActions, mToCancel)

    private var mDisabled = false
    private var mInRunLoop = false

    private lateinit var mOpMode: CommandOpMode

    val isOpModeLooping get() = mOpMode.isLooping
    
    private fun initCommand(command: Command, interruptible: Boolean, requirements: Set<Subsystem>) {
        command.init()
        val scheduledCommand = CommandState(interruptible)
        mScheduledCommands[command] = scheduledCommand
        mInitActions.forEach { it.invoke(command) }
        requirements.forEach { mRequirements[it] = command }
    }

    private fun schedule(interruptible: Boolean, command: Command) {
        if(mInRunLoop) {
            mToSchedule[command] = interruptible
            return
        }

        if(CommandGroupBase.getGroupedCommands().contains(command)) {
            throw IllegalArgumentException("A command that is part of a command group cannot be independently scheduled")
        }

//        if(mDisabled || (!command.runsWhenDisabled && mOpMode.disabled) || mScheduledCommands.containsKey(command)) {
//            return
//        }

        val requirements = command.getRequirements()

        if(Collections.disjoint(mRequirements.keys, requirements)) {
            initCommand(command, interruptible, requirements)
        } else {
            requirements.forEach {
                if(mRequirements.containsKey(it)
                        && !mScheduledCommands[mRequirements[it]]!!.isInterruptible) {
                    return
                }
            }

            requirements.forEach {
                if(mRequirements.containsKey(it)) {
                    cancel(mRequirements[it]!!)
                }
            }

            initCommand(command, interruptible, requirements)
        }
    }




    fun resetScheduler() {
        allMaps.forEach(MutableMap<*,*>::clear)
        allLists.forEach(MutableList<*>::clear)
        mDisabled = false
        mInRunLoop = false
    }

    fun printTelemetry() {
        println()
        println()
        println("scheduled command size ${mScheduledCommands.size}")
        println("requirements size ${mRequirements.size}")
        println("subsystems size ${mSubsystems.size}")
        println("to schedule size ${mToSchedule.size} ")
        println("to cancel size ${mToCancel.size}")
    }

    fun setOpMode(opMode: CommandOpMode) {
        mOpMode = opMode
    }

    fun schedule(interruptible: Boolean, vararg commands: Command) {
        commands.forEach { schedule(interruptible, it) }
    }

    fun schedule(vararg commands: Command) {
        schedule(true, *commands)
    }

    fun run() {
        if(mDisabled) {
            return
        }

        mSubsystems.keys.forEach(Subsystem::periodic)

        mInRunLoop = true
        val iterator = mScheduledCommands.keys.iterator()
        while(iterator.hasNext()) {
            val command = iterator.next()

//            if(!command.runsWhenDisabled && mOpMode.disabled) {
//                command.end(true)
//                mInterruptActions.forEach { it.invoke(command) }
//                mRequirements.keys.removeAll(command.getRequirements())
//                iterator.remove()
//                return
//            }

//            if(!command.runsWhenDisabled && mOpMode.disabled) {
//                command.cancel()
//                iterator.remove()
//                return
//            }

            command.execute()
            mExecuteActions.forEach { it.invoke(command) }

            if(command.isFinished) {
                command.end(false)
                mFinishActions.forEach { it.invoke(command) }
                iterator.remove()
                mRequirements.keys.removeAll(command.getRequirements())
            }
        }

        mInRunLoop = false

        mToSchedule.forEach { (k, v) -> schedule(v, k) }
        mToCancel.forEach { cancel(it) }

        mToSchedule.clear()
        mToCancel.clear()

        mSubsystems.forEach { (k, v) ->
            if(!mRequirements.containsKey(k) && v != null) {
                schedule(v)
            }
        }
    }

    fun registerSubsystem(vararg subsystems: Subsystem) {
        subsystems.forEach { mSubsystems[it] = null }
    }

    fun unregisterSubsystem(vararg subsystems: Subsystem) {
        mSubsystems.keys.removeAll(subsystems)
    }

    fun setDefaultCommand(subsystem: Subsystem, command: Command) {
        if(!command.getRequirements().contains(subsystem)) {
            throw IllegalArgumentException("Default commands must require subsystem")
        }

        if(command.isFinished) {
            throw java.lang.IllegalArgumentException("Default commands should not end")
        }

        mSubsystems[subsystem] = command
    }

    fun getDefaultCommand(subsystem: Subsystem): Command {
        return mSubsystems[subsystem]!!
    }

    fun cancel(vararg commands: Command) {
        if(mInRunLoop) {
            mToCancel.addAll(commands)
            return
        }

        commands.forEach {
            if(!mScheduledCommands.containsKey(it)) {
                return@forEach
            }

            it.end(true)
            mInterruptActions.forEach { action -> action.invoke(it) }
            mScheduledCommands.remove(it)
            mRequirements.keys.removeAll(it.getRequirements())
        }
    }

    fun cancelAll() {
        mScheduledCommands.keys.forEach { cancel(it) }
    }

    fun isScheduled(vararg commands: Command): Boolean {
        return mScheduledCommands.keys.containsAll(commands.toList())
    }

    fun requiring(subsystem: Subsystem): Command {
        return mRequirements[subsystem]!!
    }

    fun disable() {
        mDisabled = true
    }

    fun enable() {
        mDisabled = false
    }

    fun addPeriodic(action: () -> Unit) {
        InfiniteCommand(action).schedule()
    }

    fun scheduleWatchdog(condition: () -> Boolean, command: Command) {
        schedule(Watchdog(condition, command))
    }

    fun onCommandInit(action: (Command) -> Unit) {
        mInitActions.add(action)
    }

    fun onCommandExecute(action: (Command) -> Unit) {
        mExecuteActions.add(action)
    }

    fun onCommandInterrupt(action: (Command) -> Unit) {
        mInterruptActions.add(action)
    }

    fun onCommandFinish(action: (Command) -> Unit) {
        mFinishActions.add(action)
    }
}