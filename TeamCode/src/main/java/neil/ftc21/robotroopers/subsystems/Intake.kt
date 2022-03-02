package neil.ftc21.robotroopers.subsystems

import neil.koawalib.hardware.motor.KMotorEx
import neil.koawalib.hardware.sensor.KRev2mDistanceSensor
import neil.koawalib.subsystem.DeviceSubsystem

class Intake(private val motor: KMotorEx, private val distanceSensor: KRev2mDistanceSensor) : DeviceSubsystem() {
    companion object IntakeConstants {
        const val threshold = 76.2
    }

    var isMineralIn = false
        private set

    private var reading = false

    fun startReading() {
        reading = true
    }

    fun stopReading() {
        reading = false
    }

    fun intakeOn() {
        motor.setSpeed(1.0)
    }

    fun intakeOff() {
        motor.setSpeed(0.0)
    }

    fun intakeReverse() {
        motor.setSpeed(-1.0)
    }

    override fun periodic() {
        if(reading) {
            isMineralIn = distanceSensor.invokeDouble() < threshold
        }
    }
}