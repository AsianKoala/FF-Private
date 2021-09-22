package org.firstinspires.ftc.teamcode.control.system

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.util.opmode.AkemiTelemetry

class HardwareManager(private val hwMap: HardwareMap, private val akemiTelemetry: AkemiTelemetry, private vararg val subsystems: Subsystem) {

    fun setupMapping() {
        subsystems.forEach { it.init(hwMap) }
    }

    fun updateAll() {
        subsystems.forEach { it.update() }
    }

    fun updateTelemetryAll() {
        subsystems.forEach { akemiTelemetry.addAll(it.updateTelemetry()) }
    }

    fun stopAll() {
        subsystems.forEach { it.stop() }
    }
}
