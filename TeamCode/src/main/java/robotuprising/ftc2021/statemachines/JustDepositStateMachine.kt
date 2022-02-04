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

    private val indexer = Indexer
    private val outtake = Outtake
    private val arm = Arm
    private val turret = Turret
    private val slides = Slides

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.INDEX)
            .onEnter(indexer::index)
            .onExit(indexer::open)
            .transitionTimed(1.0)

            .state(States.COCK_OUTTAKE_ARM)
            .onEnter(outtake::cock)
            .onEnter(arm::home)
            .transitionTimed(0.5)

            .state(States.RESET_SLIDES)
            .onEnter { slides.setSlideInches(0.1) }
            .transitionTimed(2.0)

            .state(States.RESET_TURRET)
            .onEnter { turret.setTurretLockAngle(180.0) }
            .transitionTimed(1.0)

            .state(States.HOME_OUTTAKE)
            .onEnter(outtake::home)
            .transitionTimed(0.5)
            .build()

}