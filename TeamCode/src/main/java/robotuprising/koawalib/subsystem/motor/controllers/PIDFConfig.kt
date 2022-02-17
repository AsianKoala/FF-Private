package robotuprising.koawalib.subsystem.motor.controllers

data class PIDFConfig(
        val kP: Double = 0.0,
        val kI: Double = 0.0,
        val kD: Double = 0.0,

        val kV: Double = 0.0,
        val kA: Double = 0.0,
        val kStatic: Double = 0.0,
        val kF: (Double, Double?) -> Double = { _, _ -> 0.0 },

        val positionEpsilon: Double = 1.0,

        val homePositionToDisable: Double = Double.NaN,
)