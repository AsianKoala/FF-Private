package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Indexer
import robotuprising.ftc2021.subsystems.osiris.Osiris
import robotuprising.ftc2021.subsystems.osiris.OsirisState
import robotuprising.lib.system.statemachine.StateMachineBuilder

open class DepositStateMachine(private val depositGoal: OsirisState) : StateMachineI<DepositStateMachine.States>() {
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
                osiris.setGoal(depositGoal)
                deposited = false
            }
            .transition(osiris::done)
            .state(States.DEPOSIT)
            .onEnter(indexer::index)
            .onExit(indexer::open)
            .onExit { deposited = true }
            .transitionTimed(0.5)
            .state(States.RESET)
            .onEnter(osiris::setResetGoal)
            .transition(osiris::done)
            .build()
}
