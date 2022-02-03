package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Indexer
import robotuprising.ftc2021.subsystems.osiris.hardware.Intake
import robotuprising.ftc2021.subsystems.osiris.hardware.LoadingSensor
import robotuprising.ftc2021.subsystems.osiris.hardware.Outtake
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

object IntakeStateMachine : StateMachineI<IntakeStateMachine.States>() {
    enum class States {
        INTAKING,
        MINERAL_IN_REVERSE_INTAKING,
        COCK
    }

    private val intake = Intake
    private val sensor = LoadingSensor
    private val indexer = Indexer
    private val outtake = Outtake

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.INTAKING)
            .onEnter(intake::turnOn)
            .onExit(intake::turnReverse)
            .onExit(indexer::lock)
            .loop {
                OsirisDashboard.addLine("INTAKING")
            }
            .transition(sensor::isMineralIn)

            .state(States.MINERAL_IN_REVERSE_INTAKING)
            .onExit(intake::turnOff)
            .loop { OsirisDashboard.addLine("REVERSE INTAKING") }
            .transitionTimed(5.0)

            .state(States.COCK)
            .onEnter(outtake::cock)
            .transitionTimed(1.0)
            .build()

}
