package robotuprising.ftc2021.opmodes

import robotuprising.ftc2021.hardware.Akemi
import robotuprising.ftc2021.hardware.BaseRobot
import robotuprising.lib.math.Angle
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.system.BaseOpMode
import robotuprising.lib.util.opmode.OpModePacket

class BaseTeleOp : BaseOpMode() {
    override val robot: BaseRobot = Akemi(OpModePacket(Pose(Point.ORIGIN, Angle.EAST), false, hardwareMap, gamepad1, gamepad2))
}