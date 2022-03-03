package asiankoala.ftc21.v2.lib.hardware

import asiankoala.ftc21.v2.lib.math.Pose

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
