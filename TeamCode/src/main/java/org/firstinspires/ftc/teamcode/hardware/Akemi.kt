package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.control.system.HardwareManager
import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket

class Akemi(dataPacket: OpModePacket) : BaseRobot(dataPacket) {
    private val homura = Homura()
    private val intake = Intake()
    override val hwManager = HardwareManager(dataPacket.hwMap, dataPacket.aTelem, homura, intake)
}
