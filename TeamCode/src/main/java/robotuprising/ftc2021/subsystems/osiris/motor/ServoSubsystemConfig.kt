package robotuprising.ftc2021.subsystems.osiris.motor

data class ServoSubsystemConfig(
        val name: String,

        val homePosition: Double = 0.0,
        val outPosition: Double = 0.0
)