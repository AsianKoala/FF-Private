package robotuprising.ftc2021.manager

import robotuprising.ftc2021.statemachines.StateMachineI

object StateMachineManager {
    private val stateMachines = ArrayList<StateMachineI<*>>()

    fun addMachine(machine: StateMachineI<*>) {
        stateMachines.add(machine)
    }

    fun periodic() {
        stateMachines.forEach(StateMachineI<*>::update)
    }
}