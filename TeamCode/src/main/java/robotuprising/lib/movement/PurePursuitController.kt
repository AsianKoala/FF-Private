package robotuprising.lib.movement

import robotuprising.lib.movement.waypoints.LockedWaypoint
import robotuprising.lib.movement.waypoints.Waypoint
import robotuprising.lib.math.MathUtil.toRadians
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import kotlin.math.PI

object PurePursuitController {

    fun relVals(curr: Pose, target: Point): Point {
        val d = (curr.p - target).hypot
        val rh = (target - curr.p).atan2 - curr.h
        return Point(-d * rh.sin, d * rh.cos)
    }

    fun goToPosition(curr: Pose, target: Waypoint): Pose {
        val relTarget = relVals(curr, target.p)
        val movementPowers = (relTarget / 12.0)

        val deltaH = getDeltaH(curr, target)
        val turnPower = deltaH / 90.0.toRadians

        return Pose(movementPowers, Angle(turnPower, AngleUnit.RAW))
    }

    fun getDeltaH(curr: Pose, target: Waypoint): Double {
        return if (target is LockedWaypoint) {
            (target.h - curr.h).wrap().angle
        } else {
            val forward = (target.p - curr.p).atan2
            val back = forward + Angle(PI, AngleUnit.RAD)
            val angleToForward = (forward - curr.h).wrap()
            val angleToBack = (back - curr.h).wrap()
            val autoAngle = if (angleToForward.abs < angleToBack.abs) forward else back
            (autoAngle - curr.h).wrap().angle
        }
    }
}