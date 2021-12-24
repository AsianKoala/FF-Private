package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.util.WraithServo

class Marker : Subsystem {
    private val pitchServo = WraithServo("pitch")
    private val yawServo = WraithServo("yaw")
    private val extendServo = WraithServo("extend")

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun read() {
        TODO("Not yet implemented")
    }

    override fun sendDashboardData() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}