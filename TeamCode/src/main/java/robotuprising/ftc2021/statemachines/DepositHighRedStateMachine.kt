package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.Indexer
import robotuprising.ftc2021.subsystems.osiris.Osiris
import robotuprising.lib.system.statemachine.StateMachineBuilder

object DepositHighRedStateMachine : StateMachineI<DepositHighRedStateMachine.States>() {
    enum class States {
        SET_OSIRIS_GOAL,
        DEPOSIT,
        RESET
    }

    private val osiris = Osiris
    private val indexer = Indexer

    var deposited = false

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.SET_OSIRIS_GOAL)
            .onEnter {
                osiris.setGoal(osiris.depositGoalRed)
                deposited = false
            }
            .transition(osiris::done)
            .state(States.DEPOSIT)
            .onEnter(indexer::index)
            .onExit(indexer::open)
            .onExit { deposited = true }
            .transitionTimed(0.5)
            .state(States.RESET)
            .onEnter { osiris.setGoal(osiris.resetGoal) }
            .transition(osiris::done)
            .build()
}
