package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

object IntakeStateMachine : StateMachineI<IntakeStateMachine.States>() {
    enum class States {
        OUTTAKE_RESET,
        INTAKING,
        MINERAL_IN_REVERSE_INTAKING,
        COCK,
        TURN_TURRET
    }

    private val intake = Intake
    private val sensor = LoadingSensor
    private val indexer = Indexer
    private val outtake = Outtake
    private val turret = Turret


    override val stateMachine = StateMachineBuilder<States>()
            .state(States.OUTTAKE_RESET)
            .onEnter(outtake::home)
            .transitionTimed(0.2)

            .state(States.INTAKING)
            .onEnter(intake::turnOn)
            .onExit(indexer::lock)
            .loop {
                OsirisDashboard.addLine("INTAKING")
            }
            .transition(sensor::isMineralIn)

            .state(States.MINERAL_IN_REVERSE_INTAKING)
            .onEnter(intake::turnReverse)
            .onExit(intake::turnOff)
            .loop { OsirisDashboard.addLine("REVERSE INTAKING") }
            .transitionTimed(0.5)

            .state(States.COCK)
            .onEnter(outtake::cock)
            .transitionTimed(1.0)

            .state(States.TURN_TURRET)
            .onEnter { turret.setTurretLockAngle(245.0) }
            .transition { turret.isAtTarget }
            .build()

}
