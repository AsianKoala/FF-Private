package robotuprising.ftc2021.v2.statemachines.blue

import robotuprising.ftc2021.v2.statemachines.StateMachineI
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.*
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.koawalib.statemachine.StateMachineBuilder

object IntakeStateMachineBlue : StateMachineI<IntakeStateMachineBlue.States>() {
    enum class States {
        OUTTAKE_RESET,
        INTAKING,
        MINERAL_IN_REVERSE_INTAKING,
        LOCK,
        COCK,
        TURN_TURRET
    }

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
            .onEnter(Outtake::home)
            .onEnter(Indexer::open)
            .transitionTimed(0.2)

            .state(States.INTAKING)
            .onEnter(Intake::turnOn)
            .onExit { hasIntaked = true }
            .loop { OsirisDashboard.addLine("INTAKING") }
            .transition(LoadingSensor::isMineralIn)

            .state(States.MINERAL_IN_REVERSE_INTAKING)
            .onEnter(Intake::turnReverse)
            .onExit(Intake::turnOff)
            .loop { OsirisDashboard.addLine("REVERSE INTAKING") }
            .transitionTimed(0.5)

            .state(States.LOCK)
            .onEnter(Indexer::lock)
            .transitionTimed(0.6)

            .state(States.COCK)
            .onEnter(Outtake::cock)
            .transitionTimed(0.3)

            .state(States.TURN_TURRET)
            .onEnter {
                if(!shared) Turret.depositBlueHigh()
                else Turret.depositBlueShared()
            }
            .transition { Turret.isAtTarget }
            .build()

}
