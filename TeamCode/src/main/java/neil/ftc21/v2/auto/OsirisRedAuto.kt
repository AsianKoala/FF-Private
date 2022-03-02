package neil.ftc21.v2.auto

import neil.ftc21.v2.opmodes.osiris.OsirisOpMode
import neil.ftc21.v2.subsystems.osiris.hardware.Ghost
import neil.ftc21.v2.subsystems.osiris.hardware.Odometry
import neil.lib.math.AngleUnit
import neil.lib.math.Pose

class OsirisRedAuto: OsirisOpMode() {
    private val ghost = Ghost
    private val odometry = Odometry

    private val startPose = Pose(AngleUnit.RAD)

    override fun mInit() {
        super.mInit()
        odometry.startPose = startPose
    }
}