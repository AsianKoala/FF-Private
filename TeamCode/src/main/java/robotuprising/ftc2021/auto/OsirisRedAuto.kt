package robotuprising.ftc2021.auto

import robotuprising.ftc2021.opmodes.osiris.OsirisOpMode
import robotuprising.ftc2021.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
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