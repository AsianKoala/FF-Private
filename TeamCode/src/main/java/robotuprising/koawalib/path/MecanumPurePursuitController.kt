package robotuprising.koawalib.path

import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.math.MathUtil.cos
import robotuprising.koawalib.math.MathUtil.sin
import robotuprising.koawalib.math.MathUtil.wrap
import robotuprising.koawalib.path2.Waypoint
import kotlin.math.PI
import kotlin.math.absoluteValue

//object MecanumPurePursuitController {
////    val SLIP_DISTANCES = Pose()
////    val UNDERSHOOT_DISTANCE = 6.0
////    val MIN_SLIP_SPEED = 8.0
////    val GUNNING_REDUCTION_DISTANCE = Pose()
////    val CLOSE_EXPONENT = 1.0/6.0
//
//    fun relDistance(curr: Pose, target: Point): Point {
//        val d = (curr.point - target).hypot
//        val rh = (target - curr.point).atan2 - curr.heading
//        val wrapped = rh.wrap
//        return Point(-d * wrapped.sin, d * wrapped.cos)
//    }
//
//    fun goToPosition(curr: Pose, robotVelocity: Pose, target: Waypoint): Pose {
//        val relPPTarget = relDistance(curr, target.point)
//
//        val translationalPowers = relPPTarget / 12.0
//
//        val angleToTarget = if (target is LockedWaypoint) {
//            (target.h - curr.heading).wrap
//        } else {
//            val forward = (target.point - curr.point).atan2
//            val back = forward + PI
//            val angleToForward = (forward - curr.heading).wrap
//            val angleToBack = (back - curr.heading).wrap
//            val autoAngle = if (angleToForward.absoluteValue < angleToBack.absoluteValue) forward else back
//            (autoAngle - curr.heading).wrap
//        }
//
//        val rotationPower = angleToTarget / 90.0
//
//        return Pose(translationalPowers, rotationPower)
//    }
//
//
//}