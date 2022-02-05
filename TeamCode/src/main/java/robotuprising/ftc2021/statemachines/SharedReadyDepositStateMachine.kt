package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Arm
import robotuprising.ftc2021.subsystems.osiris.hardware.Outtake
import robotuprising.ftc2021.subsystems.osiris.hardware.Slides
import robotuprising.ftc2021.util.Constants
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
            .onEnter { Arm.moveServoToPosition(Constants.armSharedPosition) }
            .onEnter { Outtake.moveServoToPosition(Constants.outtakeSharedPosition) }
            .onExit { counter++ }
            .transitionTimed(1.0)

            .build()
}