package robotuprising.koawalib.path

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.math.Line
import robotuprising.koawalib.math.MathUtil
import robotuprising.koawalib.path.waypoints.StopWaypoint
import robotuprising.koawalib.path.waypoints.Waypoint

class PurePursuitPath(val waypoints: List<Waypoint>) {
    private val DEAD_MAN_SWITCH = 2000

    private var currPoint = 0
    private val timeUntilDeadMan = ElapsedTime()

    fun update(position: Pose, velocity: Pose): Pose {

        var jumpToNextSegment: Boolean
        do {
            jumpToNextSegment = false
            val target = waypoints[currPoint + 1]
            val current = waypoints[currPoint]

            if(target is StopWaypoint && timeUntilDeadMan.milliseconds() > DEAD_MAN_SWITCH) {
                jumpToNextSegment = true
            } else if(target !is StopWaypoint || velocity.point.hypot > 1.0) {
                timeUntilDeadMan.reset()
            }
            if(target is StopWaypoint) {
                if(position.point.distance(target.point) < target.allowedPositionError) {
                    jumpToNextSegment = true
                }
            } else {
                if(position.point.distance(target.point) < target.followDistance) {
                    jumpToNextSegment = true
                }
            }

            if(jumpToNextSegment) {
                currPoint++
            }

        } while(jumpToNextSegment && currPoint < waypoints.size - 1)
        if(finished) return Pose()

        val target = waypoints[currPoint + 1]

        return if(target is StopWaypoint && position.point.distance(target.point) < target.followDistance) {
            MecanumPurePursuitController.goToPosition(
                    position,
                    velocity,
                    target
            )
        } else {
            trackToLine(
                    position,
                    velocity,
                    waypoints[currPoint],
                    waypoints[currPoint + 1]
            )
        }
    }

    private fun trackToLine(robotPosition: Pose, robotVelocity: Pose, start: Waypoint, mid: Waypoint): Pose {
        val currSegment = Line(start.point, mid.point)
        val center = currSegment.nearestLinePoint(robotPosition.point)

        val intersection = MathUtil.circleLineIntersection(
                start.point, mid.point, center, mid.followDistance
        )

        val target = Waypoint(intersection.x, intersection.y, mid.followDistance)
        return MecanumPurePursuitController.goToPosition(
                robotPosition,
                robotVelocity,
                target
        )
    }

    val finished get() = currPoint >= waypoints.size - 1
}