package robotuprising.ftc2021.statemachines

import robotuprising.lib.system.statemachine.StateMachine

abstract class StateMachineI<E> {
    protected abstract val stateMachine: StateMachine<E>

    fun start() {
        stateMachine.reset()
        stateMachine.start()
    }

    fun update() {
        stateMachine.update()
    }

    fun stop() {
        stateMachine.stop()
    }

    val done get() = !stateMachine.running
}