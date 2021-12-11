package lib

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.MathUtil.degrees
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose

object AutoTesting {
    @JvmStatic
    fun main(args: Array<String>) {
        val start = Pose(Point(7.0, 63.0), Angle(90.0.radians, AngleUnit.RAD))
        val target = Pose(Point(7.0, 42.0), Angle(45.0.radians, AngleUnit.RAD))

        val relVal = relVals(start, target.p)
        println(relVal)

        val deltaH = (target.h - start.h).wrap().angle
        println(deltaH.degrees)
    }

    fun relVals(curr: Pose, target: Point): Point {
        val d = (curr.p - target).hypot
        val rh = (target - curr.p).atan2 - curr.h
        return Point(-d * rh.sin, d * rh.cos)
    }
}
