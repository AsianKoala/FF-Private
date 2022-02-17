package robotuprising.koawalib.math

import robotuprising.koawalib.util.Extensions.d
import robotuprising.koawalib.util.Extensions.wrap

data class Pose(
        val x: Double = 0.0,
        val y: Double = 0.0,
        val h: Double = 0.0
) {
    constructor(x: Int, y: Int, h: Int) : this(x.d, y.d, h.d)

    val point = Point(x,y)

    operator fun plus(other: Pose) = Pose(x + other.x, y + other.y, (h + other.h).wrap)
    operator fun minus(other: Pose) = this + -other
    operator fun unaryMinus() = Pose(-x, -y, -h)
}