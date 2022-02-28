package robotuprising.ftc2021.v3

import com.acmerobotics.dashboard.config.Config
import robotuprising.ftc2021.v3.subsystems.*
import robotuprising.koawalib.hardware.KServo
import robotuprising.koawalib.hardware.motor.KMotor
import robotuprising.koawalib.hardware.motor.KMotorEx
import robotuprising.koawalib.hardware.motor.controllers.MotionProfileConfig
import robotuprising.koawalib.hardware.motor.controllers.MotionProfileController
import robotuprising.koawalib.hardware.motor.controllers.OpenLoopController
import robotuprising.koawalib.hardware.sensor.KLimitSwitch
import robotuprising.koawalib.hardware.sensor.KRevColorSensor

class Hardware {
    @Config
    companion object HardwareConstants {
        const val FL_NAME = "fl"
        const val BL_NAME = "bl"
        const val FR_NAME = "fr"
        const val BR_NAME = "br"
        const val INTAKE_NAME = "intake"
        const val TURRET_NAME = "turret"
        const val PITCH_NAME = "pitch"
        const val SLIDE_NAME = "slides"

        const val INDEXER_NAME =  "indexer"
        const val INDEXER_START_POSITION = Indexer.LOCK_POSITION
        const val OUTTAKE_NAME = "outtake"
        const val OUTTAKE_START_POSITION = Outtake.HOME_POSITION

        const val LOADING_SENSOR_NAME = "loadingSensor"
        const val TURRET_LIMIT_SWITCH_NAME = "turretLimitSwitch"
        const val PITCH_LIMIT_SWITCH_NAME = "pitchLimitSwitch"
    }
    
    val flMotor = KMotor(FL_NAME)
    val blMotor = KMotor(BL_NAME)
    val frMotor = KMotor(FR_NAME)
    val brMotor = KMotor(BR_NAME)
    val intakeMotor = KMotorEx(INTAKE_NAME, OpenLoopController())
    val turretMotor = KMotorEx(TURRET_NAME, MotionProfileController(Turret.config))
    val pitchMotor = KMotorEx(PITCH_NAME, MotionProfileController(Slides.config))
    val slideMotor = KMotorEx(SLIDE_NAME, MotionProfileController(Pitch.config))

    val indexerServo = KServo(INDEXER_NAME).startAt(INDEXER_START_POSITION)
    val outtakeServo = KServo(OUTTAKE_NAME).startAt(OUTTAKE_START_POSITION)

    val loadingSensor = KRevColorSensor(LOADING_SENSOR_NAME)
    val turretLimitSwitch = KLimitSwitch(TURRET_LIMIT_SWITCH_NAME)
    val pitchLimitSwitch = KLimitSwitch(PITCH_LIMIT_SWITCH_NAME)
}
