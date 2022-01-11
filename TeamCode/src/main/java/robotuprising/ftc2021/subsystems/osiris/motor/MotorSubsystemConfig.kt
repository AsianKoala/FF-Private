package robotuprising.ftc2021.subsystems.osiris.motor

data class MotorSubsystemConfig(
        val motorConfig: MotorConfig,

        val controlType: MotorControlType,

        val homePosition: Double = 0.0,
        val unitsPerTick: Double = 1.0,
        val gearRatio: Double = 1.0,

        val kP: Double = 0.0,
        val kI: Double = 0.0,
        val kD: Double = 0.0,
        val kStatic: Double = 0.0,
        val kV: Double = 0.0,
        val kA: Double = 0.0,
        val kF: (Double, Double?) -> Double = { x, v -> 0.0 },

        val maxVelocity: Double = 0.0,
        val maxAcceleration: Double = 0.0,
        val maxJerk: Double = 0.0,

        val deadzone: Double = 0.0,

        val positionEpsilon: Double = 0.0,
        val positionUpperLimit: Double = Double.POSITIVE_INFINITY,
        val positionLowerLimit: Double = Double.NEGATIVE_INFINITY,

        val postZeroedValue: Double = 0.0
)