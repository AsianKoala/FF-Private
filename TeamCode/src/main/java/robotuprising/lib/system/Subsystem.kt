package robotuprising.lib.system

interface Subsystem {
    fun update()
    fun stop()
    fun sendDashboardPacket()
}
