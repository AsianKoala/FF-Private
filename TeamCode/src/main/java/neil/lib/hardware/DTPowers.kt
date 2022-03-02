package neil.lib.hardware

import neil.lib.math.Angle
import neil.lib.math.AngleUnit
import neil.lib.math.Point
import neil.lib.math.Pose

data class DTPowers(
        val x: Double,
        val y: Double,
        val turn: Double
) {
    val pose get() = Pose(Point(x, y), Angle(turn, AngleUnit.RAW))
}