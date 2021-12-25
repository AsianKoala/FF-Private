package robotuprising.ftc2021.subsystems.wraith

class Wraith : Subsystem() {

    private val ghost = Ghost
    private val turret = Turret
    private val slide = Slide
    private val intake = Intake
    private val odometry = Odometry

    private var currentState = WraithState()
    private var currentStateTarget = WraithState()
    private var goalState = WraithState()


    override fun reset() {
        TODO("Not yet implemented")
    }

    override fun updateDashboard(debugging: Boolean) {
        TODO("Not yet implemented")
    }
}