package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.DeviceSubsystem

class Duck(private val motor: KMotor) : DeviceSubsystem() {
    fun setSpeed(speed: Double) {
        motor.setSpeed(speed)
    }
}