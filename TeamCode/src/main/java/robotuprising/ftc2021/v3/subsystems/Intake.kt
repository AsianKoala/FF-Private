package robotuprising.ftc2021.v3.subsystems

import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.subsystem.motor.MotorSubsystem
import robotuprising.koawalib.subsystem.motor.controllers.OpenLoopController
import robotuprising.koawalib.subsystem.sensors.KRevColorSensor

class Intake(motor: KMotor, private val loadingSensor: KRevColorSensor) : MotorSubsystem(motor, OpenLoopController()) {
    companion object {
        const val THRESHOLD = 20.0
        const val ON_POWER = 1.0
        const val OFF_POWER = 0.0
        const val REVERSE_POWER = -1.0
    }

    fun turnOn() {
        setPower(1.0)
    }

    fun turnReverse() {
        setPower(-1.0)
    }

    fun turnOff() {
        setPower(0.0)
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