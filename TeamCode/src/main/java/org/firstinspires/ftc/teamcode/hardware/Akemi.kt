package org.firstinspires.ftc.teamcode.hardware

import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket

class Akemi(dataPacket: OpModePacket) : BaseRobot(dataPacket) {

    private val homura = Homura()

    override val hwManager = AkemiHWMImpl(homura)
}
