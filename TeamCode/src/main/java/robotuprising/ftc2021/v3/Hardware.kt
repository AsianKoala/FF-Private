package robotuprising.ftc2021.v3

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.subsystem.sensors.KRevColorSensor

class Hardware {
    @Config
    companion object {
        const val INTAKE_NAME = "intakeMotor"
        const val LOADING_SENSOR_NAME = "loadingSensor"
    }

    val intakeMotor = KMotor(INTAKE_NAME)
    val loadingSensor = KRevColorSensor(LOADING_SENSOR_NAME)

}