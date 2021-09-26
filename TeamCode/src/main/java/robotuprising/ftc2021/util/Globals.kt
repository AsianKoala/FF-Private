package robotuprising.ftc2021.util

import robotuprising.lib.math.Angle
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.opmode.AllianceSide

object Globals {
    const val TELEOP_NAME = "AkemiTele"
    const val IS_COMP = false
    var AUTO_END_POSE = Pose(Point.ORIGIN, Angle.EAST)
    var ALLIANCE_SIDE = AllianceSide.BLUE
}