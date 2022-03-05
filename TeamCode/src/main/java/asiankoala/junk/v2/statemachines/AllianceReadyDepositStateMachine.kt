package asiankoala.junk.v2.statemachines

import asiankoala.junk.v2.subsystems.hardware.Arm
import asiankoala.junk.v2.subsystems.hardware.Outtake
import asiankoala.junk.v2.subsystems.hardware.Slides
import asiankoala.junk.v2.subsystems.osiris.hardware.*
import asiankoala.koawalib.statemachine.StateMachineBuilder

// STATIC TURRET MOVEMENT
object AllianceReadyDepositStateMachine : StateMachineI<AllianceReadyDepositStateMachine.States>() {
    enum class States {
        EXTEND_SLIDES,
        MOVE_ARM_AND_OUTTAKE,
    }

    override val stateMachine = StateMachineBuilder<States>()
        .state(States.EXTEND_SLIDES)
        .onEnter(Slides::deposit)
        .transitionTimed(0.5)
        .state(States.MOVE_ARM_AND_OUTTAKE)
        .onEnter(Arm::depositHigh)
        .onEnter(Outtake::depositHigh)
        .transitionTimed(2.0)
        .build()
}
