package robotuprising.lib.system

abstract class Subsystem {
    open fun start() {}
    open fun init_loop() {}
    abstract fun update()
    abstract fun stop()
    abstract fun sendDashboardPacket()
}
