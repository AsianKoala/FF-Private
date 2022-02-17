package robotuprising.ftc2021.v2.auto

import robotuprising.ftc2021.v2.opmodes.osiris.OsirisOpMode
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.v2.subsystems.osiris.hardware.Odometry
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Pose

class OsirisRedAuto: OsirisOpMode() {
    private val ghost = Ghost
    private val odometry = Odometry

    private val startPose = Pose(AngleUnit.RAD)

    override fun mInit() {
        super.mInit()
        odometry.startPose = startPose
    }
}