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

    val drive = KMecanumOdoDrive(hardware.flMotor, hardware.blMotor, hardware.brMotor, hardware.frMotor, encoders.odo, true)
    val intake = Intake(hardware.intakeMotor, hardware.distanceSensor)
    val arm = Arm(hardware.armServo)
    val indexer = Indexer(hardware.indexerServo)
    val outtake = Outtake(hardware.outtakeServo)
    val intakeStopper = IntakeStopper(hardware.intakeStopperServo)
    val duck = Duck(hardware.duckMotor)
    val turret = Turret(hardware.turretMotor, encoders.turretEncoder)
    val slides = Slides(hardware.slideMotor, encoders.slideEncoder)

    fun log() {
        Logger.addTelemetryData("power", drive.powers.rawString())
        Logger.addTelemetryData("position", drive.position)
        Logger.addTelemetryData("turret angle", encoders.turretEncoder.position)
        Logger.addTelemetryData("slides inches", encoders.slideEncoder.position)
    }

    init {
        drive.setStartPose(startPose)
        slides.setPIDTarget(0.0)
        turret.setPIDTarget(Turret.homeAngle)
        slides.disabled = false
        turret.disabled = false
    }
}