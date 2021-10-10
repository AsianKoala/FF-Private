package robotuprising.lib.system

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.hardware.Status

abstract class Subsystem {
    abstract fun init(hwMap: HardwareMap)
    open fun start() {}
    open fun init_loop() {}
    abstract fun update()
    abstract fun stop()
    abstract fun sendDashboardPacket()
    abstract var status: Status
}
