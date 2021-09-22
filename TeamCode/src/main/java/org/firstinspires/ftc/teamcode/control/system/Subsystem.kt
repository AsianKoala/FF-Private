package org.firstinspires.ftc.teamcode.control.system

import com.qualcomm.robotcore.hardware.HardwareMap

abstract class Subsystem {
    abstract fun init(hwMap: HardwareMap)
    abstract fun update()
    abstract fun updateTelemetry(): HashMap<String, Any>
    abstract fun stop()
    abstract fun setHWValues()
}
