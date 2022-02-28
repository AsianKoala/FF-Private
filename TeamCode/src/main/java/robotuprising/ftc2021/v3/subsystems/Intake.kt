package robotuprising.ftc2021.v3.subsystems

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.motor.KMotor
import robotuprising.koawalib.hardware.sensor.KRevColorSensor
import robotuprising.koawalib.subsystem.DeviceSubsystem
import robotuprising.koawalib.subsystem.Subsystem

class Intake(private val motor: KMotor, private val loadingSensor: KRevColorSensor) : DeviceSubsystem() {
    @Config
    companion object IntakeConstants {
        const val THRESHOLD = 20.0
        const val ON_POWER = 1.0
        const val OFF_POWER = 0.0
        const val REVERSE_POWER = -1.0
    }

    fun turnOn() {
        motor.setSpeed(ON_POWER)
    }

    fun turnReverse() {
        motor.setSpeed(REVERSE_POWER)
    }

    fun turnOff() {
        motor.setSpeed(OFF_POWER)
    }


    private var lastRead = Double.NaN
    private var reading = false

    var isMineralIn = false
        private set

    fun startReading() {
        reading = true
    }

    fun stopReading() {
        reading = false
    }

    override fun periodic() {
        if(reading) {
            lastRead = loadingSensor.invokeDouble()
            isMineralIn = lastRead < THRESHOLD
        }
    }
}