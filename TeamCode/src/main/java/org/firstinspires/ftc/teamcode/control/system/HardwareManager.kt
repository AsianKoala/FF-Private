package org.firstinspires.ftc.teamcode.control.system

import com.qualcomm.robotcore.hardware.HardwareMap

abstract class HardwareManager(vararg val subsystems: Subsystem) {

    abstract fun setupMapping(hwMap: HardwareMap)

    fun updateAll() {
        subsystems.forEach { it.update() }
    }

    fun updateTelemetryAll() {
        subsystems.forEach { it.updateTelemetry() }
    }

    fun stopAll() {
        subsystems.forEach { it.stop() }
    }
}
