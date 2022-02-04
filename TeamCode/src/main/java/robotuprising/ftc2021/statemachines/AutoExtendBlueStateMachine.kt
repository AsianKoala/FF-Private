package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.ftc2021.subsystems.osiris.hardware.Slides
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.math.Point
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

object AutoExtendBlueStateMachine : StateMachineI<AutoExtendBlueStateMachine.States>() {
    enum class States {
        LOOP
    }

    private val odometry = Odometry
    private val slides = Slides

    private val hubPoint = Point()

    var enabled = false

    override fun stop() {
        super.stop()
        enabled = false
    }

    private fun loop() {
        val currentPosition = odometry.currentPosition

        val distance = currentPosition.p.distance(hubPoint)

        if(distance < Constants.slideMaxInches) {
            slides.setSlideLockTarget(distance)
        }
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.LOOP)
            .loop(AutoExtendBlueStateMachine::loop)
            .build()
}
