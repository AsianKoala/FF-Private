package robotuprising.koawalib.command.scheduler

import robotuprising.koawalib.command.CommandOpMode
import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.subsystem.Subsystem
import robotuprising.koawalib.util.Periodic
import robotuprising.lib.opmode.OpModeState
import java.util.function.BooleanSupplier

object CommandScheduler {
    private val commandMap = HashMap<Command, BooleanSupplier>()
    private val requirementMap = HashMap<Subsystem, MutableSet<Command>>()
    private val defaultMap = HashMap<Subsystem, Command>()

    private val registered = LinkedHashSet<Periodic>()

    private lateinit var opMode: CommandOpMode

    fun setOpMode(c: CommandOpMode): CommandScheduler {
        opMode = c
        return this
    }

    fun terminateOpMode(): CommandScheduler {
        opMode.terminate()
        return this
    }

    val opmodeRuntime get() = opMode.runtime

    fun resetScheduler(): CommandScheduler {
        Command.clear()
        commandMap.clear()
        requirementMap.clear()
        defaultMap.clear()
        registered.clear()
        return this
    }

    fun schedule(command: Command): CommandScheduler =
            schedule(command) { true }

    fun scheduleOnce(c: Command): CommandScheduler =
            schedule(c)

    fun scheduleOnceForState(c: Command, state: OpModeState): CommandScheduler =
            scheduleForState(c, state)

    fun scheduleInit(c: Command, supplier: BooleanSupplier): CommandScheduler =
            scheduleForState(c, supplier, OpModeState.INIT)

    fun scheduleInit(c: Command): CommandScheduler =
            scheduleForState(c, { true }, OpModeState.INIT)

    fun scheduleJoystick(c: Command, supplier: BooleanSupplier): CommandScheduler =
            scheduleForState(c, supplier, OpModeState.LOOP, OpModeState.STOP)

    fun scheduleJoystick(c: Command): CommandScheduler =
            scheduleForState(c, OpModeState.LOOP, OpModeState.STOP)


    fun scheduleForState(c: Command, vararg states: OpModeState): CommandScheduler =
            schedule(c.cancelUpon {!opMode.opModeState.isStatus(*states) }) { opMode.opModeState.isStatus(*states) }

    fun scheduleForState(c: Command, supplier: BooleanSupplier, vararg states: OpModeState): CommandScheduler =
            schedule(c.cancelUpon {!opMode.opModeState.isStatus(*states) }) { supplier.asBoolean && opMode.opModeState.isStatus(*states) }



    fun scheduleAfterOther(dependency: Command, other: Command): CommandScheduler =
            schedule(other, dependency::justFinishedNoCancel)


    fun scheduleWithOther(dependency: Command, other: Command): CommandScheduler =
            schedule(other, dependency::justStarted)


    fun scheduleAfterOther(dependency: Command, other: Command, additionalCondition: BooleanSupplier): CommandScheduler =
            schedule(other) { dependency.justFinishedNoCancel && additionalCondition.asBoolean}

    fun scheduleWithOther(dependency: Command, other: Command, additionalCondition: BooleanSupplier): CommandScheduler =
            schedule(other) { dependency.justStarted && additionalCondition.asBoolean }

    fun scheduleDefault(command: Command, subsystem: Subsystem) {
        if(command.requirements.contains(subsystem)) {
            defaultMap[subsystem] = command
            schedule(command) { getCurrent(subsystem) == command }
        } else {
            System.err.println("default commands must require their subsystem: " + command::class.java.toString());
        }
    }

    fun register(p: Periodic): CommandScheduler {
        registered.add(p)
        return this
    }

    fun getDefault(s: Subsystem): Command? {
        return if(opMode.opModeState == OpModeState.LOOP) {
            defaultMap[s]
        } else {
            null
        }
    }

    fun getCurrent(s: Subsystem): Command? {
        if(requirementMap[s] == null) return null
        for(c in requirementMap[s]!!) {
            if(c.isRunning) {
                return c
            }
        }

        return getDefault(s)
    }

    fun schedule(command: Command, supplier: BooleanSupplier): CommandScheduler {
        commandMap[command] = supplier

        for(s in command.requirements) {
            requirementMap.putIfAbsent(s, LinkedHashSet())
            requirementMap[s]!!.add(command)
            register(s)
        }

        return this
    }

    fun run() {
        commandMap.forEach { c1, b ->
            if (c1.justStarted) {
                for (subsystem in c1.requirements) {
                    for (c2 in requirementMap[subsystem]!!) {
                        if (c1 != c2) {
                            c2.cancel()
                        }
                    }
                }
            }
        }

        commandMap.forEach { c1, b ->
            if(b.asBoolean || c1.isRunning) {
                c1.run()
            }
        }

        registered.forEach(Periodic::periodic)
    }
}