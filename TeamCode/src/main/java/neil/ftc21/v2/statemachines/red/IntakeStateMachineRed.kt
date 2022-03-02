package neil.ftc21.v2.statemachines.red

import neil.ftc21.v2.statemachines.StateMachineI
import neil.ftc21.v2.subsystems.osiris.hardware.*
import neil.lib.opmode.OsirisDashboard
import neil.koawalib.statemachine.StateMachineBuilder

object IntakeStateMachineRed : StateMachineI<IntakeStateMachineRed.States>() {
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
        if(!IntakeStateMachineRed.addedToManager) {
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
                if(!shared) turret.depositRedHigh()
                else turret.depositRedShared()
            }
            .transition { turret.isAtTarget }
            .build()
}