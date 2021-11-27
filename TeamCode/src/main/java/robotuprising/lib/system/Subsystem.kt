package robotuprising.lib.system

interface Subsystem {
    fun update()
    fun sendDashboardPacket(debugging: Boolean)
    fun reset()
}
