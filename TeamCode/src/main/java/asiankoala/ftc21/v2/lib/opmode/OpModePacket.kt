package asiankoala.ftc21.v2.lib.opmode

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import asiankoala.ftc21.v2.lib.math.Pose

data class OpModePacket(
    val startPose: Pose,
    val debugging: Boolean,
    val hwMap: HardwareMap,
    val gamepad: Gamepad,
    val gamepad2: Gamepad
)
