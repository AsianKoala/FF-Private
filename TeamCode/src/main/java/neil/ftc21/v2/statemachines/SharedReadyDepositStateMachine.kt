package neil.ftc21.v2.statemachines

import neil.ftc21.v2.subsystems.osiris.hardware.Arm
import neil.ftc21.v2.subsystems.osiris.hardware.Outtake
import neil.ftc21.v2.subsystems.osiris.hardware.Slides
import neil.koawalib.statemachine.StateMachine
import neil.koawalib.statemachine.StateMachineBuilder

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