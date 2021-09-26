package robotuprising.ftc2021.control.path

import robotuprising.lib.movement.path.Path
import robotuprising.lib.movement.PurePursuitController
import robotuprising.lib.movement.waypoints.PointTurnWaypoint
import robotuprising.lib.movement.waypoints.StopWaypoint
import robotuprising.lib.movement.waypoints.Waypoint
import robotuprising.ftc2021.deprecated.OldAzusa
import robotuprising.lib.math.*
import robotuprising.lib.math.MathUtil.toRadians

class PurePursuitPath(waypoints: ArrayList<Waypoint>) : Path(waypoints) {
    override fun update(azusa: OldAzusa) {
        val currPose = azusa.currPose

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
        if (isFinished) return

        val start = waypoints[currWaypoint]
        val end = waypoints[currWaypoint + 1]

        val clip: Point = MathUtil.clipIntersection(start.p, end.p, azusa.currPose.p)
        val (clipX, clipY) = MathUtil.circleLineIntersection(clip, start.p, end.p, end.followDistance)
        val target = end.copy
        target.x = clipX
        target.y = clipY

        azusa.azuTelemetry.addData("followpoint", target.p)
        azusa.azuTelemetry.addData("target", end)
        azusa.azuTelemetry.fieldOverlay()
            .setStroke("white")
            .strokeLine(azusa.currPose.p.dbNormalize.x, azusa.currPose.p.dbNormalize.y, target.p.dbNormalize.x, target.p.dbNormalize.y)

        if ((end is StopWaypoint && azusa.currPose.distance(end) < end.followDistance) || end is PointTurnWaypoint) {
            PurePursuitController.goToPosition(azusa.currPose, end)
        } else {
            val (nx, ny) = target.p.dbNormalize
            azusa.azuTelemetry.fieldOverlay()
                .setStroke("purple")
                .strokeCircle(nx, ny, 1.0)

            val relTarget = PurePursuitController.relVals(azusa.currPose, target.p)

            val movementPowers = (relTarget / 12.0)

            val deltaH = PurePursuitController.getDeltaH(azusa.currPose, target)
            val turnPower = deltaH / 90.0.toRadians

            // get perpendicular intersetion for x component of the robot
            val dClip = currPose.p.distance(clip)
            if (dClip > 4) {
                val relClip = PurePursuitController.relVals(currPose, target.p)
                val relMovement = relClip / 4.0
                val finalMovement = (relMovement + movementPowers) / 2.0
                azusa.driveTrain.powers = Pose(finalMovement, Angle(turnPower, AngleUnit.RAW))
            } else {
                azusa.driveTrain.powers = Pose(movementPowers, Angle(turnPower, AngleUnit.RAW))
            }
        }
    }
}