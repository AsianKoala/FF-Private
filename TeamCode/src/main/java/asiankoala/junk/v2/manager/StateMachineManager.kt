package asiankoala.junk.v2.manager

import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.statemachines.StateMachineI

object StateMachineManager {
    private val stateMachines = ArrayList<StateMachineI<*>>()

    fun addMachine(machine: StateMachineI<*>) {
        stateMachines.add(machine)
    }

    fun periodic() {
        stateMachines.forEach(StateMachineI<*>::update)

        OsirisDashboard["state machines added"] = stateMachines.size
    }

    fun stop() {
        stateMachines.forEach(StateMachineI<*>::stop)
        stateMachines.clear()
    }
}
