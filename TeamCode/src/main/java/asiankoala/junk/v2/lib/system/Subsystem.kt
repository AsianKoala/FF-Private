package asiankoala.junk.v2.lib.system

interface Subsystem {
    fun update()
    fun sendDashboardPacket(debugging: Boolean)
    fun reset()
}
