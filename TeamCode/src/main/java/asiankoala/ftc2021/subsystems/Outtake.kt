package asiankoala.ftc2021.subsystems

import com.acmerobotics.dashboard.config.Config
import com.asiankoala.koawalib.hardware.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

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
