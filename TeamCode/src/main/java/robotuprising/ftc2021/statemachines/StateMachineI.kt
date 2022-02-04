package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.manager.StateMachineManager
import robotuprising.lib.system.statemachine.StateMachine

abstract class StateMachineI<E> {
    protected abstract val stateMachine: StateMachine<E>

    private val stateMachineManager = StateMachineManager
    private var addedToManager = false

    open fun stop() {
        stateMachine.stop()
        stateMachine.reset()

        addedToManager = false
    }

    open fun start() {
        if(!addedToManager) {
            stateMachineManager.addMachine(this)
            addedToManager = true
        }

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