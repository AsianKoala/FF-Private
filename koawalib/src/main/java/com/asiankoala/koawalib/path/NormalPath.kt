package com.asiankoala.koawalib.path

import com.asiankoala.koawalib.math.*
import com.asiankoala.koawalib.math.Pose
import kotlin.math.absoluteValue

class NormalPath(private val waypoints: List<NormalWaypoint>) {
    constructor(vararg waypoints: NormalWaypoint) : this(waypoints.toList())
    var isFinished = false
        private set

    fun update(pose: Pose, tol: Double): Pair<Pose, Double> {
        val extendedPath = ArrayList<NormalWaypoint>(waypoints)

        val clippedToPath = NormalPurePursuitController.clipToPath(waypoints, pose.point)
        val currFollowIndex = clippedToPath.index + 1

        // NOTE: we start running commands based on CLIPPED position
        // meaning, if the robot hasn't passed a waypoint, even if following that next waypoint's segment
        // the robot will not run the next waypoint command until after it has passed it (reflected by currFollowIndex)
//        for (waypoint in waypoints.subList(0, currFollowIndex)) {
//            Logger.addTelemetryLine("attempting to run waypoint commands in interval [0,$currFollowIndex]")
//            if (waypoint.command != null) {
//                // TODO: WAS PREIVOUSLY !waypoint.command.isFinished && !waypoint.command.isScheduled, CHECK IF WORKS
//                if (!waypoint.command.isScheduled) {
//                    Logger.addTelemetryLine("scheduled waypoint $waypoint command ${waypoint.command}")
//                    waypoint.command.schedule()
//                }
//            }
//        }

        var movementLookahead = NormalPurePursuitController.calcLookahead(
                waypoints,
                pose,
                waypoints[currFollowIndex].followDistance
        )

        val last = extendLine(
                waypoints[waypoints.size - 2].point,
                waypoints[waypoints.size - 1].point,
                waypoints[waypoints.size - 1].followDistance * 1.5
        )

        extendedPath[waypoints.size - 1] =
                extendedPath[waypoints.size - 1].copy(x = last.x, y = last.y)

        val turnLookahead = NormalPurePursuitController.calcLookahead(
                extendedPath,
                pose,
                waypoints[currFollowIndex].followDistance
        )

        val clippedDistanceToEnd = (clippedToPath.point - waypoints[waypoints.size - 1].point).hypot

        if (clippedDistanceToEnd <= movementLookahead.followDistance + 6 ||
                (pose.point - waypoints[waypoints.size - 1].point).hypot < movementLookahead.followDistance + 6
        ) {
            movementLookahead = waypoints[waypoints.size - 1]
        }

        val movePower = NormalPurePursuitController.goToPosition(
                pose,
                movementLookahead.point,
                movementLookahead.stop,
                movementLookahead.maxMoveSpeed,
                movementLookahead.maxTurnSpeed,
                movementLookahead.deccelAngle,
                movementLookahead.headingLockAngle,
                movementLookahead.minAllowedHeadingError,
                movementLookahead.lowestSlowDownFromHeadingError,
        ).point

        val absolutePointAngle = turnLookahead.headingLockAngle ?: (turnLookahead.point - pose).atan2

        val turnResult = NormalPurePursuitController.pointTo(
                absolutePointAngle,
                pose.heading,
                waypoints[currFollowIndex].maxTurnSpeed,
                waypoints[currFollowIndex].deccelAngle
        )
        val finalTurnPower = turnResult.first
        val realRelativeAngle = turnResult.second

        val errorTurnSoScaleMovement = clamp(
                1.0 - (realRelativeAngle / movementLookahead.minAllowedHeadingError).absoluteValue,
                movementLookahead.lowestSlowDownFromHeadingError,
                1.0
        )

        val finalXPower = movePower.x * errorTurnSoScaleMovement
        val finalYPower = movePower.y * errorTurnSoScaleMovement

        if (clippedDistanceToEnd < tol) {
            isFinished = true
        }

        return Pair(Pose(finalXPower, finalYPower, finalTurnPower), absolutePointAngle)
    }
}