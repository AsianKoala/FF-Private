package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Arm(private var servo: KServo) : DeviceSubsystem() {
    companion object ArmConstants {
        const val armHomePosition = 0.10
        const val armHighPosition = 0.73
        const val armSharedPosition = 1.0
    }
    
    fun home() {
        servo.position = armHomePosition
    }

    fun depositHigh() {
        servo.position = armHighPosition
    }

    fun depositShared() {
        servo.position = armSharedPosition
    }

    fun setPosition(position: Double) {
        servo.position = position
    }
}