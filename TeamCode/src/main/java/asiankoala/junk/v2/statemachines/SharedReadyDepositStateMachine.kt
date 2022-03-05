package asiankoala.junk.v2.statemachines

import asiankoala.junk.v2.subsystems.hardware.Arm
import asiankoala.junk.v2.subsystems.hardware.Outtake
import asiankoala.junk.v2.subsystems.hardware.Slides
import asiankoala.koawalib.statemachine.StateMachine
import asiankoala.koawalib.statemachine.StateMachineBuilder

object SharedReadyDepositStateMachine : StateMachineI<SharedReadyDepositStateMachine.States>() {
    enum class States {
        READY_ARM_OUTTAKE
    }

    var counter = 0

    override fun start() {
        super.start()
        counter = 0
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
        .state(States.READY_ARM_OUTTAKE)
        .onEnter(Arm::depositShared)
        .onEnter(Outtake::depositShared)
        .onEnter { Slides.setSlideInches(3.0) }
        .onExit { counter++ }
        .transitionTimed(1.0)
        .build()
}
