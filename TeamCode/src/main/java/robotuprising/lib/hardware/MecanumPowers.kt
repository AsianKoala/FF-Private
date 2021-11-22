package robotuprising.lib.hardware

import robotuprising.lib.math.Pose

class MecanumPowers {
    private var wheelPowers = mutableListOf<Double>()

    fun setPowers(powers: MutableList<Double>) {
        wheelPowers = powers
    }

    fun setPowers(powers: Pose) {
        wheelPowers = mutableListOf(
            -powers.y - powers.x - powers.h.angle,
            -powers.y + powers.x - powers.h.angle,
            powers.y - powers.x - powers.h.angle,
            powers.y + powers.x - powers.h.angle
        )
    }
}
