package robotuprising.ftc2021.v2.manager

import robotuprising.ftc2021.v2.statemachines.StateMachineI
import robotuprising.lib.opmode.OsirisDashboard

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