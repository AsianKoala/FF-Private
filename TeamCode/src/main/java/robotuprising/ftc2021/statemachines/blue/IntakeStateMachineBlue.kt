package robotuprising.ftc2021.statemachines.blue

import robotuprising.ftc2021.statemachines.StateMachineI
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

object IntakeStateMachineBlue : StateMachineI<IntakeStateMachineBlue.States>() {
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

    var hasIntaked = false

    var shared = false
    var shouldCock = false

    override fun start() {
        if(!addedToManager) {
            shared = false
        }

        super.start()
        hasIntaked = false
        shouldCock = false
    }

    override val stateMachine = StateMachineBuilder<States>()
            .state(States.OUTTAKE_RESET)
            .onEnter(outtake::home)
            .onEnter(Indexer::open)
            .transitionTimed(0.2)

            .state(States.INTAKING)
            .onEnter(intake::turnOn)
            .onExit(indexer::lock)
            .onExit { hasIntaked = true }
            .loop { OsirisDashboard.addLine("INTAKING") }
//            .transition(sensor::isMineralIn)
            .transition { shouldCock }

            .state(States.MINERAL_IN_REVERSE_INTAKING)
            .onEnter(intake::turnReverse)
            .onExit(intake::turnOff)
            .loop { OsirisDashboard.addLine("REVERSE INTAKING") }
            .transitionTimed(0.5)

            .state(States.COCK)
            .onEnter(outtake::cock)
            .transitionTimed(0.5)

            .state(States.TURN_TURRET)
            .onEnter {
                if(!shared) turret.depositBlueHigh()
                else turret.depositBlueShared()
            }
            .transition { turret.isAtTarget }
            .build()

}
