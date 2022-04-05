package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.hardware.sensor.AxesSigns
import com.asiankoala.koawalib.hardware.sensor.KIMU
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.KThreeWheelOdometry
import com.asiankoala.koawalib.subsystem.old.FeedforwardConstants
import com.asiankoala.koawalib.subsystem.old.MotorControlType
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig
import com.asiankoala.koawalib.subsystem.old.PIDConstants
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

class Hutao {
    private val hardware = Hardware()
    val encoders = Encoders(hardware)

    private val imu = KIMU("imu", AxesOrder.XYZ, AxesSigns.NPN)
    private val odo = KThreeWheelOdometry(encoders.leftEncoder, encoders.rightEncoder, encoders.auxEncoder,8.690685, 7.641969, imu)

    val drive = KMecanumOdoDrive(hardware.flMotor, hardware.blMotor, hardware.brMotor, hardware.frMotor, odo, false)
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
            maxVelocity = 160.0,
            maxAcceleration = 160.0,
            positionEpsilon = 1.0,
            homePositionToDisable = 0.0,
    ))
}