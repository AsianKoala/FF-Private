package robotuprising.lib.system

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.util.hardware.Accuracy
import robotuprising.lib.util.hardware.Status

abstract class Subsystem {
    abstract fun init(hwMap: HardwareMap)
    open fun init_loop() {}
    open fun start() {
        status = Status.RUNNING
    }
    abstract fun update()
    abstract fun sendDashboardPacket()
    abstract fun stop()
    abstract var status: Status
    abstract var acc: Accuracy
}
