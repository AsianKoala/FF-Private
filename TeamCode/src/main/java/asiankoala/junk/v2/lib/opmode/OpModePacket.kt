package asiankoala.junk.v2.lib.opmode

import asiankoala.junk.v2.lib.math.Pose
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap

data class OpModePacket(
    val startPose: Pose,
    val debugging: Boolean,
    val hwMap: HardwareMap,
    val gamepad: Gamepad,
    val gamepad2: Gamepad
)
