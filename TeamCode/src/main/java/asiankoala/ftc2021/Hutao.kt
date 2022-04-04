package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.hardware.sensor.AxesSigns
import com.asiankoala.koawalib.hardware.sensor.KDistanceSensor
import com.asiankoala.koawalib.hardware.sensor.KIMU
import com.asiankoala.koawalib.hardware.servo.KServo
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.subsystem.odometry.KThreeWheelOdometry
import com.asiankoala.koawalib.subsystem.old.FeedforwardConstants
import com.asiankoala.koawalib.subsystem.old.MotorControlType
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig
import com.asiankoala.koawalib.subsystem.old.PIDConstants
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

class Hutao {
    private val flMotor = KMotor("fl").brake.reverse
    private val blMotor = KMotor("bl").brake.reverse
    private val brMotor = KMotor("br").brake
    private val frMotor = KMotor("fr").brake
    private val intakeMotor = KMotor("intake").reverse
    private val turretMotor = KMotor("turret").brake
    private val slideMotor = KMotor("slides").float.reverse
    private val duckMotor = KMotor("duckSpinner").brake
    private val armServo = KServo("arm").startAt(Arm.armHomePosition)
    private val indexerServo = KServo("indexer").startAt(Indexer.indexerOpenPosition)
    private val outtakeServo = KServo("outtake").startAt(Outtake.outtakeCockPosition)
    private val distanceSensor = KDistanceSensor("loadingSensor")

    val turretEncoder = KEncoder(turretMotor, 5.33333).zero(Turret.turretHomeValue)
    val slideEncoder = KEncoder(slideMotor, 20.8333).reversed.zero()

    private val ticksPerUnit = 1892.3724
    private val leftEncoder = KEncoder(frMotor, ticksPerUnit, true).zero()
    private val rightEncoder = KEncoder(flMotor, ticksPerUnit, true).zero()
    private val auxEncoder = KEncoder(brMotor, ticksPerUnit, true).zero()
    private val imu = KIMU("imu", AxesOrder.XYZ, AxesSigns.NPN)
    private val odo = KThreeWheelOdometry(leftEncoder, rightEncoder, auxEncoder,8.690685, 7.641969, imu)

    val drive = KMecanumOdoDrive(flMotor, blMotor, brMotor, frMotor, odo, false)
    val intake = Intake(intakeMotor, distanceSensor)
    val arm = Arm(armServo)
    val indexer = Indexer(indexerServo)
    val outtake = Outtake(outtakeServo)
    val duck = Duck(duckMotor)
    val turret = Turret(MotorSubsystemConfig(
            turretMotor,
            turretEncoder,
            controlType = MotorControlType.POSITION_PID,
            pid = PIDConstants(
                    0.03,
                    0.03,
                    0.0007
            ),
            ff = FeedforwardConstants(
                    kStatic = 0.03
            ),
            positionEpsilon = 1.0
    ))
    val slides = Slides(MotorSubsystemConfig(
            slideMotor,
            slideEncoder,
            controlType = MotorControlType.MOTION_PROFILE,
            pid = PIDConstants(
                    kP = 0.2,
                    kD = 0.007,
            ),
            ff = FeedforwardConstants(
                    kStatic = 0.03
            ),
            maxVelocity = 160.0,
            maxAcceleration = 160.0,
            positionEpsilon = 1.0,
            homePositionToDisable = 0.0,
    ))
}