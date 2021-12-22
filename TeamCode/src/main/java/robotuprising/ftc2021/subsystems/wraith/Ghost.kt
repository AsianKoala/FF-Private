package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.util.WraithMotor

class Ghost : WraithSubsystem {
    private val fl = WraithMotor("fl", true).brake.openLoopControl.reverse
    private val bl = WraithMotor("bl", true).brake.openLoopControl.reverse
    private val br = WraithMotor("br", true).brake.openLoopControl
    private val fr = WraithMotor("fr", true).brake.openLoopControl
    private val motors = listOf(fl, bl, br, fr)

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