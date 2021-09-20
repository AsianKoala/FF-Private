package org.firstinspires.ftc.teamcode.control.system

class HardwareManager(vararg val subsystems: Subsystem) : State() {
    override fun run() {
        subsystems.forEach {
            it.update()
            it.updateTelemetry()
        }
    }

    override fun onKill() {
        subsystems.forEach { it.stop() }
    }
}