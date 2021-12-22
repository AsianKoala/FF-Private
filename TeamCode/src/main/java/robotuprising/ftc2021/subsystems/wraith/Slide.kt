package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.util.WraithMotor

class Slide : WraithSubsystem {
    private val liftMotor = WraithMotor("slide", false).float.openLoopControl

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