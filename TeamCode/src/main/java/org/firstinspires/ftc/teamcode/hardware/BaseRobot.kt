package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.control.system.HardwareManager
import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket

abstract class BaseRobot(val dataPacket: OpModePacket) {
    abstract val hwManager: HardwareManager

    open fun init() {
        hwManager.setupMapping(dataPacket.hwMap)
    }

    open fun loop() {
        hwManager.updateAll()
    }

    open fun stop() {
        hwManager.stopAll()
    }
}
