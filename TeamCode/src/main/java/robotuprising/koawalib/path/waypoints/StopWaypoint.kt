package robotuprising.koawalib.path.waypoints

import robotuprising.koawalib.math.MathUtil.d

class StopWaypoint(
    x: Double,
    y: Double,
    followDistance: Double,
    h: Double,
    val allowedPositionError: Double,
) : LockedWaypoint(x, y, followDistance, h) {
    constructor(x: Int, y: Int, followDistance: Int, h: Double, allowedPositionError: Double) :
            this(x.d, y.d, followDistance.d, h, allowedPositionError)

    override val copy: Waypoint get() = StopWaypoint(x, y, followDistance, h, allowedPositionError)
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
