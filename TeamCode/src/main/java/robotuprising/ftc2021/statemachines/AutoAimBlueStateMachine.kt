package robotuprising.ftc2021.statemachines

import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret
import robotuprising.lib.math.MathUtil.degrees
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

// very scuffed way of doing stuff do not recommend :thumbsup:
object AutoAimBlueStateMachine : StateMachineI<AutoAimBlueStateMachine.States>() {
    enum class States {
        LOOP
    }

    private val odometry = Odometry
    private val turret = Turret

    private val hubPoint = Point(-12.0, 24.0)

    var enabled = false

    override fun stop() {
        super.stop()
        enabled = false
    }

    fun startForTeleop() {
        if(!stateMachine.running) {
            start()
        }

        enabled = !enabled
    }

    private fun loop() {
        val currentPosition = odometry.currentPosition

        val absoluteAngle = (currentPosition.p - hubPoint).atan2.angle + 180.0.radians

        val absoluteAngleDeg = absoluteAngle.degrees

//        if(absoluteAngleDeg in 90.0..270.0) {
//            turret.setTurretLockAngle(absoluteAngle)
//        }

        OsirisDashboard["autoaim angle"] = absoluteAngleDeg
    }

    override val stateMachine: StateMachine<States> = StateMachineBuilder<States>()
            .state(States.LOOP)
            .loop(AutoAimBlueStateMachine::loop)
            .transition { false }
            .build()
}
