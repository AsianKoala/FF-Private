package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.Indexer
import robotuprising.ftc2021.subsystems.osiris.Intake
import robotuprising.ftc2021.subsystems.osiris.LoadingSensor
import robotuprising.lib.system.statemachine.StateMachineBuilder

/**
 * assumptions:
 * outtake is in place,
 * indexer is open,
 * intake is down
 */
object IntakeStateMachine : StateMachineI<IntakeStateMachine.States>() {
    enum class States {
        INTAKING,
        MINERAL_IN_REVERSE_INTAKING
    }

    private val intake = Intake
    private val sensor = LoadingSensor
    private val indexer = Indexer

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.INTAKING)
            .onEnter(intake::turnOn)
            .onExit(intake::turnReverse)
            .onExit(indexer::lock)
            .transition(sensor::isMineralIn)

            .state(States.MINERAL_IN_REVERSE_INTAKING)
            .onExit(intake::turnOff)
            .transitionTimed(1.0)
            .build()
}