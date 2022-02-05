package robotuprising.ftc2021.subsystems.osiris.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import robotuprising.ftc2021.subsystems.osiris.motor.*
import robotuprising.ftc2021.util.Constants

object Turret : ZeroableMotorSubsystem(
        MotorSubsystemConfig(
                MotorConfig(
                        "turret",
                        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
                ),

                controlType = MotorControlType.POSITION_PID,

                kP = 0.03, // probably need a kstatic
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


