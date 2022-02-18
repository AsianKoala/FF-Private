package robotuprising.ftc2021.v3

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.subsystem.sensors.KRevColorSensor

class Hardware {
    @Config
    companion object {
        const val INTAKE_NAME = "intakeMotor"
        const val LOADING_SENSOR_NAME = "loadingSensor"
        const val FL_NAME = "FL"
        const val BL_NAME = "BL"
        const val FR_NAME = "FR"
        const val BR_NAME = "BR"
    }

    val intakeMotor = KMotor(INTAKE_NAME)
    val loadingSensor = KRevColorSensor(LOADING_SENSOR_NAME)
    val flMotor = KMotor(FL_NAME)
    val blMotor = KMotor(BL_NAME)
    val frMotor = KMotor(FR_NAME)
    val brMotor = KMotor(BR_NAME)
}