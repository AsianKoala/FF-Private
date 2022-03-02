package neil.lib.opmode

import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import neil.lib.math.Pose

data class OpModePacket(
    val startPose: Pose,
    val debugging: Boolean,
    val hwMap: HardwareMap,
    val gamepad: Gamepad,
    val gamepad2: Gamepad
)
