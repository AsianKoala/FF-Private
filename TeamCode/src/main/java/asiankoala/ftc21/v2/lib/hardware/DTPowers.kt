package asiankoala.ftc21.v2.lib.hardware

import asiankoala.ftc21.v2.lib.math.Angle
import asiankoala.ftc21.v2.lib.math.AngleUnit
import asiankoala.ftc21.v2.lib.math.Point
import asiankoala.ftc21.v2.lib.math.Pose

data class DTPowers(
        val x: Double,
        val y: Double,
        val turn: Double
) {
    val pose get() = Pose(Point(x, y), Angle(turn, AngleUnit.RAW))
}