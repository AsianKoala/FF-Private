package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

// assumes everything is in place
object JustDepositStateMachine : StateMachineI<JustDepositStateMachine.States>() {
    enum class States {
        INDEX,
        COCK_OUTTAKE_ARM,
        RESET_SLIDES,
        RESET_TURRET,
        HOME_OUTTAKE,
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.INDEX)
            .onEnter(Indexer::index)
            .onExit(Indexer::open)
            .transitionTimed(0.5)

            .state(States.COCK_OUTTAKE_ARM)
            .onEnter(Outtake::cock)
            .onEnter(Arm::home)
            .transitionTimed(0.3)

            .state(States.RESET_SLIDES)
            .onEnter(Slides::home)
            .transitionTimed(0.8)

            .state(States.RESET_TURRET)
            .onEnter(Turret::home)
            .transitionTimed(0.3)

            .state(States.HOME_OUTTAKE)
            .onEnter(Outtake::home)
            .transition { true }
            .build()

}