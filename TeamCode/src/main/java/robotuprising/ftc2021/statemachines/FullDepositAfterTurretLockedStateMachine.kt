package robotuprising.ftc2021.statemachines

import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

object FullDepositAfterTurretLockedStateMachine : StateMachineI<FullDepositAfterTurretLockedStateMachine.States>() {
    enum class States {
        READY_FOR_DEPOSIT_RUNNING,
        DEPOSIT_RUNNING
    }

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.READY_FOR_DEPOSIT_RUNNING)
            .onEnter(ReadyForDepositStateMachine::start)
            .loop { OsirisDashboard.addLine("RFDSM AUTO RUNNING") }
            .transition(ReadyForDepositStateMachine::done)

            .state(States.DEPOSIT_RUNNING)
            .onEnter(JustDepositStateMachine::start)
            .loop { OsirisDashboard.addLine("DEPOSIT AUTO RUNNING") }
            .transition(JustDepositStateMachine::done)
            .build()
}