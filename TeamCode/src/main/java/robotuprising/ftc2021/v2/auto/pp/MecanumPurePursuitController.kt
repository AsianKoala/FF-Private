package robotuprising.ftc2021.v2.auto.pp

object MecanumPurePursuitController {
//    val SLIP_DISTANCES = Pose(Point(0.0, 0.0), Angle(0.0, AngleUnit.RAD))
//    val UNDERSHOOT_DISTANCE = 6.0
//    val MIN_SLIP_SPEED = 8.0
//    val GUNNING_REDUCTION_DISTANCE = Pose(Point(0.0, 0.0), Angle(PI, AngleUnit.RAD))
//    val CLOSE_EXPONENT = 1.0/6.0
//
//    fun relDistance(curr: Pose, target: Point): Point {
//        val d = (curr.p - target).hypot
//        val rh = (target - curr.p).atan2 - curr.h
//        return Point(-d * rh.sin, d * rh.cos)
//    }
//
//    fun goToPosition(curr: Pose, robotVelocity: Pose, target: Waypoint): Pose {
//        val finalWaypoint: Waypoint? = if(target is StopWaypoint) target else null
//
//        val relPPTarget = relDistance(curr, target.point)
//
//        val translationalPowers = relPPTarget / 12.0
//
//        val angleToTarget = if (target is LockedWaypoint) {
//            (target.h - curr.h).wrap().angle
//        } else {
//            val forward = (target.point - curr.p).atan2
//            val back = forward + Angle(PI, AngleUnit.RAD)
//            val angleToForward = (forward - curr.h).wrap()
//            val angleToBack = (back - curr.h).wrap()
//            val autoAngle = if (angleToForward.abs < angleToBack.abs) forward else back
//            (autoAngle - curr.h).wrap().angle
//        }
//
//        val rotationPower = angleToTarget / 90.0
//
//        return Pose(translationalPowers, Angle(rotationPower, AngleUnit.RAD))
//    }
//

}