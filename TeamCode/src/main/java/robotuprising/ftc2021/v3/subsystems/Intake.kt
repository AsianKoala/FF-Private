package robotuprising.ftc2021.v3.subsystems

import robotuprising.koawalib.hardware.motor.KMotor
import robotuprising.koawalib.hardware.sensor.KRevColorSensor
import robotuprising.koawalib.subsystem.Subsystem

class Intake(private val motor: KMotor, private val loadingSensor: KRevColorSensor) : Subsystem {
    companion object {
        const val THRESHOLD = 20.0
        const val ON_POWER = 1.0
        const val OFF_POWER = 0.0
        const val REVERSE_POWER = -1.0
    }

    fun turnOn() {
        motor.setSpeed(1.0)
    }

    fun turnReverse() {
        motor.setSpeed(-1.0)
    }

    fun turnOff() {
        motor.setSpeed(0.0)
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
        isMineralIn = false
    }

    override fun periodic() {
        super.periodic()

        if(reading) {
            lastRead = loadingSensor.invokeDouble()
            isMineralIn = lastRead < THRESHOLD
        }
    }
}