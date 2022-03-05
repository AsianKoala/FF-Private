package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.subsystems.motor.MotorConfig
import asiankoala.junk.v2.subsystems.motor.MotorControlType
import asiankoala.junk.v2.subsystems.motor.MotorSubsystemConfig
import asiankoala.junk.v2.subsystems.motor.ZeroableMotorSubsystem
import asiankoala.junk.v2.util.*
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

object Slides : ZeroableMotorSubsystem(
    MotorSubsystemConfig(
        MotorConfig(
            "slides",
            zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT,
            direction = DcMotorSimple.Direction.REVERSE
        ),

        controlType = MotorControlType.MOTION_PROFILE,

        ticksPerUnit = 20.8333,
        gearRatio = 1.0,

        kP = 0.2,
        kD = 0.007,

        maxVelocity = 160.0,
        maxAcceleration = 160.0,

        positionEpsilon = 1.0,
        homePositionToDisable = 0.0,

        kStatic = 0.03,
//                kD = 0.00005,

        postZeroedValue = Constants.slideHomeValue
    )
) {
    // target is in continuous form
    fun setSlideInches(target: Double) {
        generateAndFollowMotionProfile(position, target)
    }

    fun setSlideLockTarget(target: Double) {
        setControllerTarget(target)
    }

    fun deposit() {
        setSlideInches(Constants.slideDepositInches)
    }

    fun home() {
        setSlideInches(Constants.slideHomeValue)
    }

    fun shared() {
        setSlideInches(Constants.slideSharedInches)
    }
}
