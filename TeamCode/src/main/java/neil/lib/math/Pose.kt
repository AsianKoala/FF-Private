package neil.lib.math

import com.acmerobotics.roadrunner.geometry.Pose2d

data class Pose(
    val p: Point,
    val h: Angle
) {
    constructor(unit: AngleUnit): this(Point(), Angle(0.0, unit))

    val x get() = p.x
    val y get() = p.y
    val cos get() = h.cos
    val sin get() = h.sin
    val hypot get() = p.hypot
    val copy get() = Pose(p, h)

    val pose2d get() = Pose2d(p.x, p.y, h.angle)

    fun distance(p2: Pose) = p.distance(p2.p)
//    fun distance(p2: Waypoint) = p.distance(p2.point)

    operator fun plus(other: Pose) = Pose(p + other.p, h + other.h)
    operator fun minus(other: Pose) = Pose(p - other.p, h - other.h)

    val toRawString = String.format("%.2f, %.2f, %.2f", x, y, h.angle)
    override fun toString() = String.format("%.2f, %.2f, %.2f", x, y, h.wrap().deg)
}
