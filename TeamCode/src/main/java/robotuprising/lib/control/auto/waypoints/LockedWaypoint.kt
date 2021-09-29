package robotuprising.lib.control.auto.waypoints

import robotuprising.lib.math.Angle

open class LockedWaypoint(
        x: Double,
        y: Double,
        followDistance: Double,
        var h: Angle,
) : Waypoint(x, y, followDistance) {

    override val copy: Waypoint get() = LockedWaypoint(x, y, followDistance, h)
    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f, %s, %s",
            x,
            y,
            followDistance,
            h,
        )
    }
}
