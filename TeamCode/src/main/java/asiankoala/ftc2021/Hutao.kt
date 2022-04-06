package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.old.FeedforwardConstants
import com.asiankoala.koawalib.subsystem.old.MotorControlType
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig
import com.asiankoala.koawalib.subsystem.old.PIDConstants
import com.asiankoala.koawalib.util.Logger

class Hutao(startPose: Pose) {
    private val hardware = Hardware()
    val encoders = Encoders(hardware)

    val drive = KMecanumOdoDrive(hardware.flMotor, hardware.blMotor, hardware.brMotor, hardware.frMotor, encoders.odo, false)
    val intake = Intake(hardware.intakeMotor, hardware.distanceSensor)
    val arm = Arm(hardware.armServo)
    val indexer = Indexer(hardware.indexerServo)
    val outtake = Outtake(hardware.outtakeServo)
    val duck = Duck(hardware.duckMotor)
    val turret = Turret(MotorSubsystemConfig(
            hardware.turretMotor,
            encoders.turretEncoder,
            controlType = MotorControlType.POSITION_PID,
            pid = PIDConstants(
                    0.05,
                    0.035,
                    0.0007
            ),
            ff = FeedforwardConstants(
                    kStatic = 0.042
            ),
            positionEpsilon = 1.0
    ))
    val slides = Slides(MotorSubsystemConfig(
            hardware.slideMotor,
            encoders.slideEncoder,
            controlType = MotorControlType.MOTION_PROFILE,
            pid = PIDConstants(
                    kP = 0.2,
                    kD = 0.007,
            ),
            ff = FeedforwardConstants(
                    kStatic = 0.03
            ),
            maxVelocity = 180.0,
            maxAcceleration = 160.0,
            positionEpsilon = 1.0,
            homePositionToDisable = -0.5,
    ))

    fun log() {
        Logger.addTelemetryData("power", drive.powers.rawString())
        Logger.addTelemetryData("position", drive.position)
        Logger.addTelemetryData("turret angle", encoders.turretEncoder.position)
        Logger.addTelemetryData("slides inches", encoders.slideEncoder.position)
    }

    init {
        drive.setStartPose(startPose)
        slides.setPIDTarget(0.0)
        turret.setPIDTarget(180.0)
        slides.disabled = false
        turret.disabled = false
    }
}