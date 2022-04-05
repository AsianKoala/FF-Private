package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Outtake(private val servo: KServo) : DeviceSubsystem() {
    companion object OuttakeConstants {
        const val outtakeHomePosition = 0.23
        const val outtakeCockPosition = 0.75
        const val outtakeHighPosition = 0.75
        const val outtakeSharedPosition = 1.0
    }

    fun home() {
        servo.position = outtakeHomePosition
    }

    fun cock() {
        servo.position = outtakeCockPosition
    }

    fun depositHigh() {
        servo.position = outtakeHighPosition
    }

    fun depositShared() {
        servo.position = outtakeSharedPosition
    }
}