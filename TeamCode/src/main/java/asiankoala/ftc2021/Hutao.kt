package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.AxesSigns
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.sensor.KIMU
import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.Encoder
import com.asiankoala.koawalib.subsystem.odometry.ThreeWheelOdometry
import com.asiankoala.koawalib.subsystem.old.MotorControlType
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

class Hutao {
    private val flMotor = KMotor("fl").reverse.brake
    private val blMotor = KMotor("bl").reverse.brake
    private val brMotor = KMotor("br").brake
    private val frMotor = KMotor("fr").brake
    private val intakeMotor = KMotor("intake").reverse
    private val turretMotor = KMotor("turret").brake
    private val slideMotor = KMotor("slides").float.reverse
    private val armServo = KServo("arm").startAt(Arm.armHomePosition)
    private val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    private val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)
    private val distanceSensor = KDistanceSensor("loadingSensor")

    val turretEncoder = Encoder(turretMotor, 5.33333).zero(Turret.turretHomeValue)
    val slideEncoder = Encoder(slideMotor, 20.8333).reversed.zero()

    private val ticksPerUnit = 1892.3724
    val leftEncoder = Encoder(frMotor, ticksPerUnit, true).zero()
    val rightEncoder = Encoder(flMotor, ticksPerUnit, true).zero()
    val auxEncoder = Encoder(brMotor, ticksPerUnit, true).zero()
    val imu = KIMU("imu", AxesOrder.XYZ, AxesSigns.NPN)
    val odo = BetterThreeWheelOdometry(leftEncoder, rightEncoder, auxEncoder,8.690685, 7.641969)

    val drive = KMecanumOdoDrive(flMotor, blMotor, frMotor, brMotor, odo, true)
    val intake = Intake(intakeMotor, distanceSensor)
    val arm = Arm(armServo)
    val indexer = Indexer(indexerServo)
    val outtake = Outtake(outtakeServo)
    val turret = Turret(MotorSubsystemConfig(
            turretMotor,
            turretEncoder,
            controlType = MotorControlType.POSITION_PID,
            kP = 0.03,
            kI = 0.03,
            kD = 0.0007,
            kStatic = 0.03,
            positionEpsilon = 1.0
    ))
    val slides = Slides(MotorSubsystemConfig(
            slideMotor,
            slideEncoder,
            controlType = MotorControlType.MOTION_PROFILE,
            kP = 0.2,
            kD = 0.007,
            kStatic = 0.03,
            maxVelocity = 160.0,
            maxAcceleration = 160.0,
            positionEpsilon = 1.0,
            homePositionToDisable = 0.0,
    ))
}