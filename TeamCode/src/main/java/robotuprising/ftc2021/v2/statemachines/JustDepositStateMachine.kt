package robotuprising.ftc2021.v2.statemachines

import robotuprising.ftc2021.v2.subsystems.osiris.hardware.*
import robotuprising.ftc2021.v2.util.Constants
import robotuprising.koawalib.statemachine.StateMachine
import robotuprising.koawalib.statemachine.StateMachineBuilder

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
            .transitionTimed(0.5) // prev 0.5

            .state(States.COCK_OUTTAKE_ARM)
            .onEnter { Outtake.moveServoToPosition(Constants.outtakeCockMore) }
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
            .onEnter(Indexer::open)
            .transitionTimed(0.2)

            .build()

}