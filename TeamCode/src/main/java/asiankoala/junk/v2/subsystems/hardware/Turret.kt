package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.MotorConfig
import asiankoala.junk.v2.subsystems.motor.MotorControlType
import asiankoala.junk.v2.subsystems.motor.MotorSubsystemConfig
import asiankoala.junk.v2.subsystems.motor.ZeroableMotorSubsystem
import asiankoala.junk.v2.util.Constants
import com.qualcomm.robotcore.hardware.DcMotor

object Turret : ZeroableMotorSubsystem(
    MotorSubsystemConfig(
        MotorConfig(
            "turret",
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        ),

        controlType = MotorControlType.POSITION_PID,

        kP = 0.03, // probably need a kstatic
        kI = 0.01,
        kD = 0.0007,
        kStatic = 0.01,

        ticksPerUnit = 5.33333,

        positionEpsilon = 1.0,

        postZeroedValue = Constants.turretPostZeroValue
    )
) {

    val turretAngle: Double get() = position

    fun setTurretProfileTarget(angle: Double) {
        generateAndFollowMotionProfile(turretAngle, angle)
    }

    fun setTurretLockAngle(angle: Double) {
        setControllerTarget(angle)
    }

    override fun init() {
        super.init()
        zero() // TODO ONLY ZERO IN TELEOP
    }

    fun home() {
        setTurretLockAngle(Constants.turretHomeValue)
    }

    fun depositBlueHigh() {
        setTurretLockAngle(Constants.turretBlueAngle)
    }

    fun depositBlueShared() {
        setTurretLockAngle(Constants.turretSharedBlueAngle)
    }

    fun depositRedHigh() {
        setTurretLockAngle(Constants.turretRedAngle)
    }

    fun depositRedShared() {
        setTurretLockAngle(Constants.turretSharedRedAngle)
    }
}
