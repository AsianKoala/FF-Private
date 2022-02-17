package robotuprising.ftc2021.v2.statemachines.red

import robotuprising.ftc2021.v2.statemachines.StateMachineI
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Spinner
import robotuprising.ftc2021.v2.util.Constants
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

object DuckRedStateMachine : StateMachineI<DuckRedStateMachine.States>() {
    enum class States {
        SPIN
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.SPIN)
            .onEnter { Spinner.setPower(-Constants.duckSpeed) }
            .onExit { Spinner.setPower(0.0) }
            .transitionTimed(2.3)
            .build()
}