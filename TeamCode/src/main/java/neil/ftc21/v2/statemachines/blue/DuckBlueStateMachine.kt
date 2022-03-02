package neil.ftc21.v2.statemachines.blue

import neil.ftc21.v2.statemachines.StateMachineI
import neil.ftc21.v2.subsystems.osiris.hardware.Spinner
import neil.ftc21.v2.util.Constants
import neil.koawalib.statemachine.StateMachine
import neil.koawalib.statemachine.StateMachineBuilder

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