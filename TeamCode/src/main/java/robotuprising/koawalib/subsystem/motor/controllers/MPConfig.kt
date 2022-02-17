package robotuprising.koawalib.subsystem.motor.controllers

data class MPConfig(
        val pidConfig: PIDFConfig,
        val maxVelocity: Double = 0.0,
        val maxAcceleration: Double = 0.0,
        val maxJerk: Double = 0.0
)