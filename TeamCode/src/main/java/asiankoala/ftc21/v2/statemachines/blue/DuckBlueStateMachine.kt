package asiankoala.ftc21.v2.statemachines.blue

import asiankoala.ftc21.v2.statemachines.StateMachineI
import asiankoala.ftc21.v2.subsystems.osiris.hardware.Spinner
import asiankoala.ftc21.v2.util.Constants
import asiankoala.koawalib.statemachine.StateMachine
import asiankoala.koawalib.statemachine.StateMachineBuilder

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