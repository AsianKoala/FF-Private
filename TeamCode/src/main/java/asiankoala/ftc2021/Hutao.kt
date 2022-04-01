package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.AxesSigns
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.sensor.KIMU
import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.Encoder
import com.asiankoala.koawalib.subsystem.odometry.TwoWheelOdometry
import com.asiankoala.koawalib.subsystem.old.MotorControlType
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

class Hutao {
    private val flMotor = KMotor("fl").brake.reverse
    private val blMotor = KMotor("bl").brake.reverse
    private val brMotor = KMotor("br").brake
    private val frMotor = KMotor("fr").brake
    private val intakeMotor = KMotor("intake").reverse
    private val turretMotor = KMotor("turret").brake
    private val slideMotor = KMotor("slides").float.reverse
    private val armServo = KServo("arm").startAt(Arm.armHomePosition)
    private val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    private val outtakeServo = KServo("outtake").startAt(Outtake.outtakeHomePosition)
    private val distanceSensor = KDistanceSensor("loadingSensor")

    private val turretEncoder = Encoder(turretMotor, 5.33333)
    private val slideEncoder = Encoder(slideMotor, 20.8333)

    private val ticksPerUnit = 1892.3724
    private val leftEncoder = Encoder(flMotor, ticksPerUnit, true)
    private val perpEncoder = Encoder(brMotor, ticksPerUnit, true)
    private val imu = KIMU("imu", AxesOrder.XYZ, AxesSigns.NPN)
    private val odo = TwoWheelOdometry(imu, leftEncoder, perpEncoder, 8.690685, 7.641969)

    val drive = KMecanumOdoDrive(flMotor, blMotor, frMotor, brMotor, odo, false)
    val intake = Intake(intakeMotor, distanceSensor)
    val arm = Arm(armServo)
    val indexer = Indexer(indexerServo)
    val outtake = Outtake(outtakeServo)
    val turret = Turret(MotorSubsystemConfig(
            turretMotor,
            turretEncoder,
            controlType = MotorControlType.POSITION_PID,
            kP = 0.03,
            kI = 0.01,
            kD = 0.0007,
            kStatic = 0.01,
            positionEpsilon = 2.0
    ))
    val slides = Slides(MotorSubsystemConfig(
            slideMotor,
            slideEncoder,
            controlType = MotorControlType.POSITION_PID,
            kP = 0.2,
            kD = 0.007,
            kStatic = 0.03,
            maxVelocity = 120.0,
            maxAcceleration = 120.0,
            positionEpsilon = 1.0,
            homePositionToDisable = 0.0,
    ))
}