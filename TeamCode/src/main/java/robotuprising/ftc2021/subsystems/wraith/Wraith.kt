package robotuprising.ftc2021.subsystems.wraith

class Wraith : WraithSubsystem {
    private val ghost = Ghost()
    private val intake = Intake()
    private val turret = Turret()
    private val slide = Slide()
    private val spinner = Spinner()
    private val marker = Marker()

    private val subsystems = listOf(
            ghost,
            intake,
            turret,
            slide,
            spinner,
            marker
    )


    override fun update() {
        subsystems.forEach { it.update() }
    }

    override fun read() {
        subsystems.forEach { it.read() }
    }

    override fun sendDashboardData() {
        subsystems.forEach { it.sendDashboardData() }
    }

    override fun reset() {
        subsystems.forEach { it.reset() }
    }

}