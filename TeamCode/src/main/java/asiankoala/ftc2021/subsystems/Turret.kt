package asiankoala.ftc2021.subsystems

import com.asiankoala.koawalib.subsystem.old.MotorSubsystem
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig

class Turret(config: MotorSubsystemConfig) : MotorSubsystem(config) {
    companion object TurretConstants {
        private const val allianceDiff = 69.0
        private const val sharedDiff = 90.0
        const val zeroAngle = 180.0
        const val homeAngle = 182.0
        const val blueAngle = homeAngle + allianceDiff
        const val redAngle = homeAngle - allianceDiff
        const val sharedBlueAngle = homeAngle - sharedDiff
        const val sharedRedAngle = homeAngle + sharedDiff
    }
}