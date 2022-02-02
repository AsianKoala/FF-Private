package robotuprising.lib.math

import com.acmerobotics.roadrunner.geometry.Pose2d
import robotuprising.lib.control.auto.waypoints.Waypoint

data class Pose(
    val p: Point,
    val h: Angle
) {
    constructor(unit: AngleUnit): this(Point(), Angle(0.0, unit))

    val x = p.x
    val y = p.y
    val cos = h.cos
    val sin = h.sin
    val hypot = p.hypot
    val copy = Pose(p, h)

    val pose2d = Pose2d(p.x, p.y, h.angle)

    fun distance(p2: Pose) = p.distance(p2.p)
    fun distance(p2: Waypoint) = p.distance(p2.p)

    operator fun plus(other: Pose) = Pose(p + other.p, h + other.h)
    operator fun minus(other: Pose) = Pose(p - other.p, h - other.h)

    val toRawString = String.format("%.2f, %.2f, %.2f", x, y, h.angle)
    override fun toString() = String.format("%.2f, %.2f, %.2f", x, y, h.wrap().deg)
}
