package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.subsystem.old.*

class Turret(turretMotor: KMotor, turretEncoder: KEncoder) : MotorSubsystem(MotorSubsystemConfig(
        turretMotor,
        turretEncoder,
        controlType = MotorControlType.POSITION_PID,
        pid = PIDConstants(
                0.05,
//                0.035,
                0.0007
        ),
        ff = FeedforwardConstants(
                kStatic = 0.01
        ),
        positionEpsilon = 1.0,
)) {
    companion object TurretConstants {
        private const val allianceDiff = 69.0

        const val zeroAngle = 183.0
        const val homeAngle = 183.0
        const val blueAngle = 180 + allianceDiff - 10.0
        const val redAngle = 180.0 - allianceDiff

        const val sharedBlueAngle = 90.0
        const val sharedRedAngle = 245.0

        private const val autoDiff = 64.0
        const val autoBlueAngle = 180 + autoDiff - 10.0 // blueAngle
        const val autoRedAngle = 180 - autoDiff - 10.0// redAngle

        const val fuckyAngle = 80.0
    }
}