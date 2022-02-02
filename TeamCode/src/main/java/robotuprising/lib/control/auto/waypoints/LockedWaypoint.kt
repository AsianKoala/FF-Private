package robotuprising.lib.control.auto.waypoints

import robotuprising.ftc2021.auto.pp.Subroutines
import robotuprising.lib.math.Angle

open class LockedWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    val h: Angle,
    action: Subroutines.Subroutine? = null
) : Waypoint(x, y, followDistance, action) {

    override val copy: Waypoint get() = LockedWaypoint(x, y, followDistance, h, action)
    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f, %s",
            x,
            y,
            followDistance,
            h
        )
    }
}
