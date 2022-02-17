package robotuprising.lib.control.auto.waypoints

import robotuprising.ftc2021.v2.auto.pp.Subroutines
import robotuprising.lib.math.Angle
import robotuprising.lib.util.Extensions.d

class StopWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    h: Angle,
    val allowedPositionError: Double,
    action: Subroutines.Subroutine? = null
) : LockedWaypoint(x, y, followDistance, h, action) {
    constructor(x: Int, y: Int, followDistance: Int, h: Angle, allowedPositionError: Double, action: Subroutines.Subroutine? = null) :
            this(x.d, y.d, followDistance.d, h, allowedPositionError, action)

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
