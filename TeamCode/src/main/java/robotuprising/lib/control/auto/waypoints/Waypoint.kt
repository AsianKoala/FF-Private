package robotuprising.lib.control.auto.waypoints

import robotuprising.ftc2021.auto.pp.Subroutines
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose

// id love for this to be a dataclass but yeah sucks to suck
open class Waypoint(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val followDistance: Double = 0.0,
    val action: Subroutines.Subroutine? = null
) {
    val p = Point(x, y)

    open val copy: Waypoint get() = Waypoint(x, y, followDistance, action)
    fun distance(p2: Waypoint) = p.distance(p2.p)
    fun distance(p2: Pose) = p.distance(p2.p)

    override fun toString(): String {
        return String.format(
            "%.1f, %.1f, %.1f",
            x,
            y,
            followDistance,
        )
    }
}
