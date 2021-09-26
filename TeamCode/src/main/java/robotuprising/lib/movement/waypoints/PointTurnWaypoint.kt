package robotuprising.lib.movement.waypoints

import robotuprising.lib.movement.pathfunc.Func
import robotuprising.lib.math.Angle

class PointTurnWaypoint(
        x: Double,
        y: Double,
        followDistance: Double,
        h: Angle,
        var dh: Angle,
        func: Func? = null
) : LockedWaypoint(x, y, followDistance, h, func) {

    override val copy: Waypoint get() = PointTurnWaypoint(x, y, followDistance, h, dh, func)
    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f, %s, %s, %s",
            x,
            y,
            followDistance,
            h,
            dh,
            func
        )
    }
}
