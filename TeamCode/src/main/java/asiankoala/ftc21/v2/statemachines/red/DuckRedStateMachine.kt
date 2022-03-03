package asiankoala.ftc21.v2.statemachines.red

import asiankoala.ftc21.v2.statemachines.StateMachineI
import asiankoala.ftc21.v2.subsystems.osiris.hardware.Spinner
import asiankoala.ftc21.v2.util.Constants
import asiankoala.koawalib.statemachine.StateMachine
import asiankoala.koawalib.statemachine.StateMachineBuilder

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