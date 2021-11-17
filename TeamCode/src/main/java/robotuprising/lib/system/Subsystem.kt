package robotuprising.lib.system

abstract class Subsystem {
    abstract fun update()
    abstract fun stop()
    abstract fun sendDashboardPacket()
}
