package robotuprising.ftc2021.v2.statemachines.blue

import robotuprising.ftc2021.v2.statemachines.StateMachineI
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Spinner
import robotuprising.ftc2021.v2.util.Constants
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

object DuckBlueStateMachine : StateMachineI<DuckBlueStateMachine.States>() {
    enum class States {
        SPIN,
        FAST
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.SPIN)
            .onEnter { Spinner.setPower(Constants.duckSpeed) }
            .transitionTimed(1.0)

            .state(States.FAST)
            .onEnter { Spinner.setPower(1.0) }
            .onExit { Spinner.setPower(0.0) }
            .transitionTimed(0.2)
            .build()
}