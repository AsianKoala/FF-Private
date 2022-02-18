package robotuprising.koawalib.path.waypoints

import robotuprising.koawalib.math.MathUtil.d
import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose

// id love for this to be a dataclass but yeah sucks to suck
open class Waypoint(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val followDistance: Double = 0.0,
) {
    constructor(x: Int, y: Int, followDistance: Int) : this(x.d, y.d, followDistance.d)
    val point = Point(x, y)

    open val copy: Waypoint get() = Waypoint(x, y, followDistance)
    fun distance(p2: Waypoint) = point.distance(p2.point)
    fun distance(p2: Pose) = point.distance(p2.point)

    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f",
            x,
            y,
            followDistance,
        )
    }
}
