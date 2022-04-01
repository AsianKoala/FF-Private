package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.subsystem.intake.IntakeConfig
import com.asiankoala.koawalib.subsystem.intake.KIntake

class Intake(motor: KMotor, private val sensor: KDistanceSensor) : KIntake(motor, IntakeConfig(1.0)) {
    companion object IntakeConstants {
        const val SENSOR_THRESHOLD = 20.0
    }
    private var isReadingSensor = false
    private var lastRead = Double.POSITIVE_INFINITY

    fun startReading() {
        isReadingSensor = true
        lastRead = Double.POSITIVE_INFINITY
    }

    fun stopReading() {
        isReadingSensor = false
        lastRead = Double.POSITIVE_INFINITY
    }

    val hasMineral get() = sensor.lastRead < SENSOR_THRESHOLD

    override fun periodic() {
        if(isReadingSensor) {
            lastRead = sensor.invokeDouble()
        }
    }
}