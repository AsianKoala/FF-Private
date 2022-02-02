package robotuprising.lib.control.auto.waypoints

import robotuprising.ftc2021.auto.pp.Subroutines
import robotuprising.lib.math.Angle

class StopWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    h: Angle,
    val allowedPositionError: Double,
    action: Subroutines.Subroutine? = null
) : LockedWaypoint(x, y, followDistance, h, action) {

    override val copy: Waypoint get() = StopWaypoint(x, y, followDistance, h, allowedPositionError, action)
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
