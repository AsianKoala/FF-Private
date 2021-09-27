package robotuprising.ftc2021.hardware

import robotuprising.lib.util.opmode.OpModePacket

abstract class BaseRobot(val dataPacket: OpModePacket) {
    protected abstract val superstructure: Superstructure

    open fun init() {
        superstructure.init(dataPacket.hwMap)
    }

    open fun init_loop() {

    }

    open fun start() {
        superstructure.start()
    }

    open fun loop() {
        superstructure.update()
        superstructure.sendDashboardPacket()
    }

    open fun stop() {
        superstructure.stop()
    }

    open fun teleopLoop() {

    }
}
