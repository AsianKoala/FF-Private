package asiankoala.junk.v2.auto

import asiankoala.junk.v2.lib.math.AngleUnit
import asiankoala.junk.v2.lib.math.Pose
import asiankoala.junk.v2.opmodes.OsirisOpMode
import asiankoala.junk.v2.subsystems.hardware.Ghost
import asiankoala.junk.v2.subsystems.hardware.Odometry

class OsirisRedAuto : OsirisOpMode() {
    private val ghost = Ghost
    private val odometry = Odometry

    private val startPose = Pose(AngleUnit.RAD)

    override fun mInit() {
        super.mInit()
        odometry.startPose = startPose
    }
}
