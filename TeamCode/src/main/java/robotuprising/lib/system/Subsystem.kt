package robotuprising.lib.system

import com.qualcomm.robotcore.hardware.HardwareMap

abstract class Subsystem {
    abstract fun init(hwMap: HardwareMap)
    abstract fun update()
    abstract fun sendDashboardPacket()
    abstract fun stop()
}
