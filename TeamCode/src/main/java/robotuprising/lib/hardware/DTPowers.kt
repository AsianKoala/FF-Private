package robotuprising.lib.hardware

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose

data class DTPowers(
        val x: Double,
        val y: Double,
        val turn: Double
) {
    val pose get() = Pose(Point(x, y), Angle(turn, AngleUnit.RAW))
}