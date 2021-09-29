package robotuprising.lib.control.auto.path

import robotuprising.lib.control.auto.waypoints.PointTurnWaypoint
import robotuprising.lib.control.auto.waypoints.StopWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.*
import robotuprising.lib.math.MathUtil.toRadians
import robotuprising.lib.hardware.MecanumPowers

class PurePursuitPath(waypoints: ArrayList<Waypoint>) : Path(waypoints) {



    override fun update(currPose: Pose): MecanumPowers {

        var skip: Boolean
//        do {
//            val target = waypoints[currWaypoint + 1]
//
//            skip = when (target) {
//                is StopWaypoint -> currPose.distance(target) < 0.8 && MathUtil.angleThresh(currPose.h, target.h, target.dh)
//                is PointTurnWaypoint -> MathUtil.angleThresh(currPose.h, target.h, target.dh)
//                else -> currPose.distance(target) < target.followDistance
//            }
//            5
//            val startAction = waypoints[currWaypoint].func
//            if (startAction is RepeatFunction) {
//                startAction.run(azusa, this)
//            } else if (startAction is LoopUntilFunction) {
//                skip = startAction.run(azusa, this)
//            }
//
//            if (skip) {
//                currWaypoint++
//
//                val currAction = waypoints[currWaypoint].func
//                if (currAction is SimpleFunction) {
//                    currAction.run(azusa, this)
//                }
//            }
//        } while (skip && currWaypoint < waypoints.size - 1)
        if (isFinished) return MecanumPowers()

        val start = waypoints[currWaypoint]
        val end = waypoints[currWaypoint + 1]

        val clip: Point = MathUtil.clipIntersection(start.p, end.p, currPose.p)
        val (clipX, clipY) = MathUtil.circleLineIntersection(clip, start.p, end.p, end.followDistance)
        val target = end.copy
        target.x = clipX
        target.y = clipY

//        azusa.azuTelemetry.addData("followpoint", target.p)
//        azusa.azuTelemetry.addData("target", end)
//        azusa.azuTelemetry.fieldOverlay()
//            .setStroke("white")
//            .strokeLine(currPose.p.dbNormalize.x, currPose.p.dbNormalize.y, target.p.dbNormalize.x, target.p.dbNormalize.y)

        if ((end is StopWaypoint && currPose.distance(end) < end.followDistance) || end is PointTurnWaypoint) {
            return PurePursuitController.goToPosition(currPose, end)
        } else {
//            val (nx, ny) = target.p.dbNormalize
//            azusa.azuTelemetry.fieldOverlay()
//                .setStroke("purple")
//                .strokeCircle(nx, ny, 1.0)

            val relTarget = PurePursuitController.relVals(currPose, target.p)

            val movementPowers = (relTarget / 12.0)

            val deltaH = PurePursuitController.getDeltaH(currPose, target)
            val turnPower = deltaH / 90.0.toRadians

            // get perpendicular intersetion for x component of the robot
            val dClip = currPose.p.distance(clip)
            return if (dClip > 4) {
                val relClip = PurePursuitController.relVals(currPose, target.p)
                val relMovement = relClip / 4.0
                val finalMovement = (relMovement + movementPowers) / 2.0
                MecanumPowers(finalMovement.x, finalMovement.y, Angle(turnPower, AngleUnit.RAW))
            } else {
                MecanumPowers(movementPowers.x, movementPowers.y, Angle(turnPower, AngleUnit.RAW))
            }
        }
    }
}
