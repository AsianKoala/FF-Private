package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Spinner
import robotuprising.lib.system.statemachine.StateMachineBuilder

open class DuckStateMachine(private val multiplier: Int): StateMachineI<DuckStateMachine.States>() {
    enum class States {
        SLOW,
        FAST
    }

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.SLOW)
            .onEnter { Spinner.setPower(0.5 * multiplier)}
            .transitionTimed(1.0)
            .state(States.FAST)
            .onEnter { Spinner.setPower(1.0 * multiplier) }
            .transitionTimed(0.2)
            .onExit { Spinner.setPower(0.0) }
            .build()
}

object DuckRedStateMachine: DuckStateMachine(1)
object DuckBlueStateMachine: DuckStateMachine(-1)