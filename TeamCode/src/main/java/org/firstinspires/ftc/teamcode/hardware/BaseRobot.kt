package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket

abstract class BaseRobot(val dataPacket: OpModePacket) {
    protected abstract val superstructure: Superstructure

    open fun init() {
        superstructure.init(dataPacket.hwMap)
    }

    open fun init_loop() {

    }

    open fun start() {

    }

    open fun loop() {
        superstructure.update()
        superstructure.sendDashboardPacket()
    }

    open fun stop() {
        superstructure.stop()
    }
}
