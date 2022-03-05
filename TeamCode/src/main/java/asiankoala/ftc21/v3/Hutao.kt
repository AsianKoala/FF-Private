package asiankoala.ftc21.v3

import asiankoala.ftc21.v3.subsystems.*
import com.asiankoala.koawalib.subsystem.odometry.OdoConfig
import com.acmerobotics.dashboard.config.Config
import com.asiankoala.koawalib.control.MotionProfileController
import com.asiankoala.koawalib.control.OpenLoopController
import com.asiankoala.koawalib.hardware.KServo
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.motor.KMotorEx
import com.asiankoala.koawalib.hardware.sensor.KLimitSwitch
import com.asiankoala.koawalib.hardware.sensor.KRevColorSensor
import kotlin.math.max
import kotlin.math.min

class Hutao {
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

    private val flMotor = KMotor(FL_NAME)
    private val blMotor = KMotor(BL_NAME).reverseEncoder
    private val frMotor = KMotor(FR_NAME).reverseEncoder
    private val brMotor = KMotor(BR_NAME)
    private val intakeMotor = KMotorEx(INTAKE_NAME, OpenLoopController())
    private val turretMotor = KMotorEx(TURRET_NAME, MotionProfileController(Turret.config))
    private val pitchMotor = KMotorEx(PITCH_NAME, MotionProfileController(Slides.config))
    private val slideMotor = KMotorEx(SLIDE_NAME, MotionProfileController(Pitch.config))

    private val odoLeftEncoder = frMotor
    private val odoRightEncoder = blMotor
    private val odoAuxEncoder = brMotor

    private val indexerServo = KServo(INDEXER_NAME).startAt(INDEXER_START_POSITION)
    private val outtakeServo = KServo(OUTTAKE_NAME).startAt(OUTTAKE_START_POSITION)

    private val loadingSensor = KRevColorSensor(LOADING_SENSOR_NAME)
    private val turretLimitSwitch = KLimitSwitch(TURRET_LIMIT_SWITCH_NAME)
    private val pitchLimitSwitch = KLimitSwitch(PITCH_LIMIT_SWITCH_NAME)

    
    val drive = Drive(flMotor, blMotor, frMotor, brMotor,
            OdoConfig(1892.3724, 8.690685, 7.641969,
                    odoLeftEncoder, odoRightEncoder, odoAuxEncoder)
    )

    val intake = Intake(intakeMotor, loadingSensor)
    val turret = Turret(turretMotor, turretLimitSwitch)
    val pitch = Pitch(pitchMotor, pitchLimitSwitch)
    val slides = Slides(slideMotor)

    val indexer = Indexer(indexerServo)
    val outtake = Outtake(outtakeServo)

    var hub = Hub.ALLIANCE_HIGH

    fun incHub() {
        hub = Hub.values()[min(hub.ordinal+1, Hub.values().size-1)]
    }

    fun decHub() {
        hub = Hub.values()[max(hub.ordinal-1, 0)]
    }
}