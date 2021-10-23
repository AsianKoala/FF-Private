package robotuprising.lib.hardware

import robotuprising.lib.math.Angle

class MecanumPowers(var x: Double = 0.0, var y: Double = 0.0, var h: Angle = Angle.EAST) {
    val toMotor: MutableList<Double>
        get() = mutableListOf(
                -y - x + h.angle,
                y - x + h.angle,
                -y + x + h.angle,
                y + x + h.angle,
        )

    fun set(other: MecanumPowers) {
        x = other.x
        y = other.y
        h = other.h
    }
}
