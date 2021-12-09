package robotuprising.ftc2021.opmodes.examples

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.opmodes.examples.exHwMap
import com.qualcomm.robotcore.hardware.DistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

class exIntake(hardwareMap: HardwareMap) {
    private val hwMap: exHwMap
    private val sensor: DistanceSensor
    fun turnIntakeOn() {
        hwMap.intake.power = 1.0
    }

    val distanceSensorReading: Double
        get() = sensor.getDistance(DistanceUnit.MM)

    init {
        hwMap = exHwMap(hardwareMap)
        sensor = hardwareMap.get(DistanceSensor::class.java, "sensor")
    }
}