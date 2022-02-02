package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

// assumes everything is in place
object JustDepositStateMachine : StateMachineI<JustDepositStateMachine.States>() {
    enum class States {
        INDEX,
        COCK_OUTTAKE_ARM,
        RESET_TURRET_SLIDES
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
            .transitionTimed(3.0)

            .state(States.RESET_TURRET_SLIDES)
            .onEnter {
                turret.setTurretLockAngle(Constants.turretHomeValue)
                slides.setSlideLockTarget(Constants.slideHomeValue)
            }
            .transition { turret.isAtTarget && slides.isAtTarget }
            .build()

}