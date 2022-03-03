package asiankoala.ftc21.v2.lib.control.auto.path

import asiankoala.koawalib.path.Waypoint
import asiankoala.ftc21.v2.lib.math.Angle
import asiankoala.ftc21.v2.lib.math.AngleUnit
import asiankoala.ftc21.v2.lib.math.MathUtil.radians
import asiankoala.ftc21.v2.lib.math.Point
import asiankoala.ftc21.v2.lib.math.Pose

object PurePursuitController {

    fun relVals(curr: Pose, target: Point): Point {
        val d = (curr.p - target).hypot
        val rh = (target - curr.p).atan2 - curr.h
        return Point(-d * rh.sin, d * rh.cos)
    }

    fun curve(curr: Pose, target: Waypoint, moveScale: Double = 16.0, turnScale: Double = 90.0): Pose {
//        val relTarget = relVals(curr, target.point)
//        val movementPowers = (relTarget / moveScale)

//        val deltaH = getDeltaH(curr, target)
//        val turnPower = deltaH / turnScale.radians
//
//        NakiriDashboard["PP current: "] = curr
//        NakiriDashboard["target waypoint"] = target
//        NakiriDashboard["rel target"] = relTarget
//        NakiriDashboard["delta h degrees"] = deltaH.degrees
//        NakiriDashboard["movement powers"] = movementPowers
//
//        return Pose(Point(movementPowers.x, movementPowers.y), Angle(turnPower, AngleUnit.RAW))
        return Pose(AngleUnit.RAW)
    }

    fun getDeltaH(curr: Pose, target: Waypoint): Double {
//        return if (target is LockedWaypoint) {
//            (target.h - curr.h).wrap().angle
//        } else {
//            val forward = (target.point - curr.p).atan2
//            val back = forward + Angle(PI, AngleUnit.RAD)
//            val angleToForward = (forward - curr.h).wrap()
//            val angleToBack = (back - curr.h).wrap()
//            val autoAngle = if (angleToForward.abs < angleToBack.abs) forward else back
//            (autoAngle - curr.h).wrap().angle
//        }

        return 0.0
    }

    fun turn(curr: Pose, targetHeading: Angle, turnScale: Double = 90.0): Pose {
        val dh = (targetHeading - curr.h).wrap().angle
        val turnPower = dh / turnScale.radians
        return Pose(Point(), Angle(turnPower, AngleUnit.RAW))
    }
}
