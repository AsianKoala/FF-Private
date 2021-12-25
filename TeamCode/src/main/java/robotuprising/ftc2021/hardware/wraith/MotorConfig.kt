package robotuprising.ftc2021.hardware.wraith

data class MotorConfig(
        val gearRatio: Double,
        val ticksPerRev: Double = ENCODER_RESOLUTION * gearRatio
) {
    companion object {
        const val FREE_SPEED = 6000.0 // rpm
        const val ENCODER_RESOLUTION = 28.0

        val GB_19_2 = MotorConfig(19.2, 537.7)
        val GB_13_7 = MotorConfig(13.7, 384.5)
        val GB_5_2 = MotorConfig(5.2, 145.1)
        val GB_3_7 = MotorConfig(3.7, 103.8)
    }
}