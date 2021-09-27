package robotuprising.ftc2021.hardware

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.hardware.MecanumPowers
import robotuprising.lib.util.opmode.OpModePacket

class Akemi(dataPacket: OpModePacket) : BaseRobot(dataPacket) {
    override val superstructure = Superstructure()

    override fun teleopLoop() {
        val dtpowers = MecanumPowers(
                        dataPacket.gamepad.left_stick_x * 0.75,
                        -dataPacket.gamepad.left_stick_y * 0.75,
                Angle(-dataPacket.gamepad.right_stick_x * 0.75, AngleUnit.RAW))
        superstructure.requestHomuraPowers(dtpowers)
    }
}
