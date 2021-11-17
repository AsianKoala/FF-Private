package robotuprising.ftc2021.util

import robotuprising.lib.math.Angle

data class NakiriLocalizerPacket(
    val wheelPositions: List<Double>,
    val heading: Angle,
    val trackWidth: Double,
    val wheelBase: Double,
    val lateralMultiplier: Double,
    val wheelVelocities: List<Double>?,
    val headingVelocity: Double? // angle.rad / dt
)
