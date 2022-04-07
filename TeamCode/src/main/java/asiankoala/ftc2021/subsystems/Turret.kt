package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.subsystem.old.MotorSubsystem
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig

class Turret(config: MotorSubsystemConfig) : MotorSubsystem(config) {
    companion object TurretConstants {
        private const val allianceDiff = 69.0

        const val zeroAngle = 183.0
        const val homeAngle = 183.0
        const val blueAngle = 180 + allianceDiff
        const val redAngle = 180.0 - allianceDiff
        const val sharedBlueAngle = 90.0
        const val sharedRedAngle = 270.0
    }
}