package robotuprising.ftc2021.subsystems.wraith

interface WraithSubsystem {
    fun update()
    fun read()
    fun sendDashboardData()
    fun reset()
}