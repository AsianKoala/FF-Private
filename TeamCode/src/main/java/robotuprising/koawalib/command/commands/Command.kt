package robotuprising.koawalib.command.commands

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.koawalib.command.group.ParallelCommandGroup
import robotuprising.koawalib.command.group.ParallelDeadlineGroup
import robotuprising.koawalib.command.group.ParallelRaceGroup
import robotuprising.koawalib.command.group.SequentialCommandGroup
import robotuprising.koawalib.subsystem.Subsystem
import java.util.function.BooleanSupplier
import java.util.function.DoubleSupplier
import java.util.function.Supplier

interface Command : Runnable, Supplier<CommandState> {
    companion object {
        private val stateMap: MutableMap<Command, CommandState> = HashMap()
        private val timeMap: MutableMap<Command, ElapsedTime> = HashMap()
        private val requirementMap: MutableMap<Command, Set<Subsystem>> = HashMap()

        fun clear() {
            stateMap.clear()
            timeMap.clear()
            requirementMap.clear()
        }

        fun create(c: Command, vararg s: Subsystem) {
            c.addRequirements(*s)
        }
    }

    fun addRequirements(vararg requirements: Subsystem): Set<Subsystem> {
        requirementMap.putIfAbsent(this, LinkedHashSet())
        return requirementMap[this]!!
    }

    fun initialize() {

    }

    fun execute()

    fun isFinished(): Boolean {
        return true
    }

    fun end(cancel: Boolean) {

    }


    // run command after
    fun andThen(vararg c: Command): SequentialCommandGroup {
        return SequentialCommandGroup(this, if(c.size == 1) c[0] else ParallelCommandGroup(*c))
    }

    // pause
    fun sleep(sec: Double): SequentialCommandGroup {
        return andThen(WaitCommand(sec))
    }

    fun sleep(sec: DoubleSupplier): SequentialCommandGroup {
        return andThen(WaitCommand(sec))
    }

    // await condition
    fun waitUntil(condition: BooleanSupplier): SequentialCommandGroup {
        return andThen(ConditionalCommand(condition))
    }

    // run in parallel
    fun alongWith(vararg c: Command): ParallelCommandGroup {
        val c1 = ArrayList<Command>()
        c1.add(this)
        c.forEach { c1.add(it) }
        return ParallelCommandGroup(*(c1.toTypedArray()))
    }

    fun deadline(vararg c: Command): ParallelDeadlineGroup {
        return ParallelDeadlineGroup(this, *c)
    }

    fun raceWith(vararg c: Command): ParallelRaceGroup {
        val c1 = ArrayList<Command>()
        c1.add(this)
        c.forEach { c1.add(it) }
        return ParallelRaceGroup(*(c1.toTypedArray()))
    }



    fun asConditional(condition: BooleanSupplier): ConditionalCommand {
        return ConditionalCommand(condition, this)
    }

    fun withTimeout(seconds: Double): ParallelRaceGroup {
        return raceWith(WaitCommand(seconds))
    }

    fun cancelUpon(condition: BooleanSupplier): ParallelRaceGroup {
        return raceWith(ConditionalCommand(condition))
    }






    override fun run() {
        when(getState()) {
            CommandState.RESET -> {
                getRuntime().reset()
                setState(CommandState.STARTED)
                return
            }

            CommandState.STARTED -> {
                setState(CommandState.INITIALIZING)
                return
            }

            CommandState.INITIALIZING -> {
                initialize()
                setState(CommandState.EXECUTING)
            }

            CommandState.EXECUTING -> {
                execute()
                if(isFinished()) setState(CommandState.FINISHED)
                return
            }

            CommandState.INTERRUPTING -> {
                setState(CommandState.CANCELLED)
                return
            }

            CommandState.CANCELLED -> {}

            CommandState.FINISHED -> {
                end(isCancelled)
                setState(CommandState.RESET)
            }
        }
    }




    fun getRuntime(): ElapsedTime {
        return timeMap.putIfAbsent(this, ElapsedTime()) ?: timeMap[this]!!
    }

    fun getState(): CommandState = stateMap.getOrDefault(this, CommandState.RESET)

    fun setState(s: CommandState): Command {
        stateMap[this] = s
        return this
    }

    val requirements: Set<Subsystem> get() {
        requirementMap.putIfAbsent(this, LinkedHashSet())
        return requirementMap[this]!!
    }

    val justFinished get() = getState() == CommandState.FINISHED || getState() == CommandState.CANCELLED
    val justFinishedNoCancel get() = getState() == CommandState.FINISHED
    val justStarted get() = getState() == CommandState.STARTED
    val isRunning get() = getState() != CommandState.RESET
    val isCancelled get() = getState() == CommandState.CANCELLED

    fun cancel() {
        if(isRunning && !justFinished) {
            setState(CommandState.INTERRUPTING)
        }
    }

    override fun get(): CommandState {
        return getState()
    }
}