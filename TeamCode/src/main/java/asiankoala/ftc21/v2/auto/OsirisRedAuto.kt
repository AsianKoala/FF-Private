package asiankoala.ftc21.v2.auto

import asiankoala.ftc21.v2.opmodes.osiris.OsirisOpMode
import asiankoala.ftc21.v2.subsystems.osiris.hardware.Ghost
import asiankoala.ftc21.v2.subsystems.osiris.hardware.Odometry
import asiankoala.ftc21.v2.lib.math.AngleUnit
import asiankoala.ftc21.v2.lib.math.Pose

class OsirisRedAuto: OsirisOpMode() {
    private val ghost = Ghost
    private val odometry = Odometry

    private val startPose = Pose(AngleUnit.RAD)

    override fun mInit() {
        super.mInit()
        odometry.startPose = startPose
    }
}