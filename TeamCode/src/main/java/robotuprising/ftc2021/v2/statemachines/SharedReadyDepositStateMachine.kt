package robotuprising.ftc2021.v2.statemachines

import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Arm
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Outtake
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Slides
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

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