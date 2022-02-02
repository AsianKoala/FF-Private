package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.Osiris
import robotuprising.ftc2021.subsystems.osiris.OsirisState
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

// STATIC TURRET MOVEMENT
class DepositStateMachine(private val turretTarget: Double) : StateMachineI<DepositStateMachine.States>() {
    enum class States {
        MOVE_TURRET_TO_TARGET,
        MOVE_SLIDES,
        MOVE_ARM_OUTTAKE,
        CHECK_MOVEMENT
    }

    private val turret = Turret
    private val slides = Slides
    private val arm = Arm
    private val outtake = Outtake

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.MOVE_TURRET_TO_TARGET)
            .onEnter { turret.setTurretLockAngle(turretTarget) }
            .transitionTimed(1.0)

            .state(States.MOVE_SLIDES)
            .onEnter { slides.setSlideLockTarget(Constants.slideHighInches) }
            .transitionTimed(1.0)

            .state(States.MOVE_ARM_OUTTAKE)
            .onEnter(arm::depositHigh)
            .onEnter(outtake::depositHigh)
            .transitionTimed(1.0)

            .state(States.CHECK_MOVEMENT)
            .transition { turret.isAtTarget && slides.isAtTarget }
            .build()
}
