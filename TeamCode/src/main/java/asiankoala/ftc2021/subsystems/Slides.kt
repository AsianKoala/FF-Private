package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.subsystem.old.*

class Slides(slideMotor: KMotor, slideEncoder: KEncoder) : MotorSubsystem(MotorSubsystemConfig(
        slideMotor,
        slideEncoder,
        controlType = MotorControlType.MOTION_PROFILE,
        pid = PIDConstants(
                kP = 0.23,
                kD = 0.007,
        ),
        ff = FeedforwardConstants(
                kStatic = 0.01
        ),
        maxVelocity = 180.0,
        maxAcceleration = 180.0,
        positionEpsilon = 1.5,
        homePositionToDisable = 0.0,
)) {
    companion object SlideConstants {
        const val slideHomeValue = 0.0
        const val blueDepositInches = 33.5
        const val redDepositInches = 35.0
        const val sharedInches = 7.5
        const val sharedExtInches = 12.0
        const val autoInches = 36.5 // 33.5
        var movingShared = 7.5
    }
}