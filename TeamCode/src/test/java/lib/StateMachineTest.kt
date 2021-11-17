package lib

import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

object StateMachineTest {
    var conditionTest = false

    enum class MyStates {
        INIT,
        INIT_LOOP,
        START,
        LOOP,
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val stateMachine = buildStateMachine()

        stateMachine.start()
        while (stateMachine.running)
            stateMachine.update()
        println()
        stateMachine.start()
        stateMachine.update()
    }

    private fun buildStateMachine(): StateMachine<MyStates> {
        return StateMachineBuilder<MyStates>()
            .state(MyStates.INIT)
            .onEnter { println("initting") }
            .onExit { println("finished initting ") }
            .transition { true }
            .state(MyStates.START)
            .transition { true }
            .state(MyStates.INIT_LOOP)
            .onEnter { println("started init_loop") }
            .loop { println("init looping") }
            .onExit { println("finished init looping") }
            .transition { true }
            .build()
    }
}
