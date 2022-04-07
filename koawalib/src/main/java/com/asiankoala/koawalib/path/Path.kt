package com.asiankoala.koawalib.path

import com.asiankoala.koawalib.math.*
import com.asiankoala.koawalib.util.Logger
import kotlin.math.absoluteValue

/**
 * Path movements should be continuous and fluid, and as such commands shouldn't adapt to the path,
 * rather the path should be adapted to the commands.
 */
class Path(private val waypoints: List<Waypoint>) {

    var isFinished = false
        private set

    fun update(pose: Pose, tol: Double): Pair<Pose, Double> {
        val extendedPath = ArrayList<Waypoint>(waypoints)

        val clippedToPath = PurePursuitController.clipToPath(waypoints, pose.point)
        val currFollowIndex = clippedToPath.index + 1

        var movementLookahead = PurePursuitController.calcLookahead(
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

        val turnLookahead = PurePursuitController.calcLookahead(
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

        val movePower = PurePursuitController.goToPosition(
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

        val turnResult = PurePursuitController.pointTo(
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

        val finalXPower = movePower.x/* * errorTurnSoScaleMovement*/
        val finalYPower = movePower.y * errorTurnSoScaleMovement

        if (clippedDistanceToEnd < tol) {
            isFinished = true
        }

        Logger.logInfo("pure pursuit debug started")
        Logger.logInfo("pose: $pose")
        Logger.logInfo("curr follow index: $currFollowIndex")
        Logger.logInfo("absolute point angle: ${absolutePointAngle.degrees}")
        Logger.logInfo("lookahead: ${movementLookahead.point}")
        Logger.logInfo("clipped distance: $clippedDistanceToEnd")
        Logger.logInfo("relative angle: ${realRelativeAngle.degrees}")
        Logger.logInfo("pure pursuit debug ended")

        return Pair(Pose(finalXPower, finalYPower, finalTurnPower), absolutePointAngle)
    }
}
