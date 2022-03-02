package neil.lib.math

import neil.lib.math.MathUtil.degrees
import kotlin.math.* // ktlint-disable no-wildcard-imports

data class Angle(
    val angle: Double,
    val unit: AngleUnit
) {

    val deg: Double get() = angle.degrees

    val cos get() = cos(angle)
    val sin get() = sin(angle)
    val abs get() = angle.absoluteValue
    val copy get() = Angle(angle, unit)

    fun wrap(): Angle {
        var heading = angle
        while (heading < -PI)
            heading += 2 * PI
        while (heading > PI)
            heading -= 2 * PI
        return Angle(heading, unit)
    }

    operator fun plus(other: Angle) = when (unit) {
        AngleUnit.RAD -> Angle(angle + other.angle, unit).wrap()
        AngleUnit.RAW -> Angle(angle + other.angle, unit)
    }

    operator fun minus(other: Angle) = plus(other.unaryMinus())

    operator fun unaryMinus() = when (unit) {
        AngleUnit.RAD -> Angle(-angle, unit).wrap()
        AngleUnit.RAW -> Angle(-angle, unit)
    }

    operator fun times(scalar: Double) = Angle(angle * scalar, unit)
    operator fun div(scalar: Double) = Angle(angle / scalar, unit)
}
