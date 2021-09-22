package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.control.system.HardwareManager
import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket

abstract class BaseRobot(val dataPacket: OpModePacket) {
    protected abstract val hwManager: HardwareManager

    open fun init() {
        hwManager.setupMapping()
    }

    open fun loop() {
        hwManager.updateAll()
        hwManager.updateTelemetryAll()
    }

    open fun stop() {
        hwManager.stopAll()
    }
}
