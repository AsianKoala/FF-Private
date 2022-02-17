package robotuprising.lib.control.auto.waypoints

import robotuprising.ftc2021.v2.auto.pp.Subroutines
import robotuprising.lib.math.Angle
import robotuprising.lib.util.Extensions.d

open class LockedWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    val h: Angle,
    action: Subroutines.Subroutine? = null
) : Waypoint(x, y, followDistance, action) {
    constructor(x: Int, y: Int, followDistance: Int, h: Angle, action: Subroutines.Subroutine? = null) :
            this(x.d, y.d, followDistance.d, h, action)

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
