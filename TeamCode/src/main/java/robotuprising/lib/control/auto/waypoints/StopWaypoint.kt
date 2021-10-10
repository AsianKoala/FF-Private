package robotuprising.lib.control.auto.waypoints

import robotuprising.lib.math.Angle

class StopWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    h: Angle,
    val dh: Angle,
) : LockedWaypoint(x, y, followDistance, h) {

    override val copy: Waypoint get() = StopWaypoint(x, y, followDistance, h, dh)
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
