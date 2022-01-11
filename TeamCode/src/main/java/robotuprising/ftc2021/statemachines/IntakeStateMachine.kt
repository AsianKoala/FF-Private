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
class IntakeStateMachine {
    private val intake = Intake
    private val sensor = LoadingSensor
    private val indexer = Indexer

    private enum class State {
        INTAKING,
        MINERAL_IN
    }

    private val stateMachine = StateMachineBuilder<State>()
            .state(State.INTAKING)
            .onEnter(intake::turnOn)
            .onExit(intake::turnOff)
            .onExit(indexer::lock)
            .transition(sensor::isMineralIn)
            .build()

    fun start() {
        stateMachine.reset()
        stateMachine.start()
    }

    fun update() {
        stateMachine.update()
    }

    fun stop() {
        stateMachine.stop()
    }

    val done = stateMachine.state == State.MINERAL_IN
}