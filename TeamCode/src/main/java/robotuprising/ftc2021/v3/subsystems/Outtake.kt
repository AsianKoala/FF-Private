package robotuprising.ftc2021.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.KServo
import robotuprising.koawalib.subsystem.DeviceSubsystem

class Outtake(private val servo: KServo) : DeviceSubsystem() {
    @Config
    companion object OuttakeConstants {
        const val HOME_POSITION = 0.0
        const val DEPOSIT_POSITION = 0.0
    }

    fun setPosition(position: Double) {
        servo.position = position
    }
}