package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

// very scuffed way of doing stuff do not recommend :thumbsup:
object AutoAimBlueStateMachine : StateMachineI<AutoAimBlueStateMachine.States>() {
    enum class States {
        LOOP
    }

    private val odometry = Odometry
    private val turret = Turret

    var enabled = false

    private fun loop() {
        if(enabled) {
            val currentPosition = odometry.currentPosition

            val absoluteAngle
        }
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.LOOP)

            .build()
}

/*
assume we are on red





 */