package robotuprising.ftc2021.subsystems.wraith

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Constants
import robotuprising.ftc2021.util.WraithMotor
import robotuprising.lib.opmode.WraithDashboard

object Intake : MotorSubsystem(
        ControlledMotorConfig(
                "intake",
                false,

                MotorControlType.OPEN_LOOP
        )
) {
    private val loadingSensor = BulkDataManager.hwMap[RevColorSensorV3::class.java, "intakeSensor"]

    private var lastLoadingSensorRead = Double.POSITIVE_INFINITY

    val isMineralIn = lastLoadingSensorRead < Constants.loadingSensorThreshold

    fun turnOn() {
        motor.power = 1.0
    }

    fun turnReverse() {
        motor.power = -1.0
    }

    fun turnOff() {
        motor.power = 0.0
    }

    override fun read() {
        super.read()

        lastLoadingSensorRead = loadingSensor.getDistance(DistanceUnit.MM)
    }
}