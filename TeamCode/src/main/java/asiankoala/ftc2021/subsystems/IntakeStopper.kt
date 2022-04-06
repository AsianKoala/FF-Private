package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class IntakeStopper(private val servo: KServo) : DeviceSubsystem() {
    companion object {
        const val UNLOCK_POSITION = 0.2
        const val LOCK_POSITION = 0.75
    }

    fun unlock() {
        servo.position = UNLOCK_POSITION
    }

    fun lock() {
        servo.position = LOCK_POSITION
    }
}