package robotuprising.ftc2021.statemachines

import robotuprising.lib.system.statemachine.StateMachine

abstract class StateMachineI<E> {
    protected abstract val stateMachine: StateMachine<E>

    fun start() {
        stateMachine.stop()
        stateMachine.reset()
        stateMachine.start()
    }

    fun update() {
        if(stateMachine.running) {
            stateMachine.update()
        }
    }

    val done get() = !stateMachine.running
}