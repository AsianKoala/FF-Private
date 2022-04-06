package asiankoala.ftc2021.subsystems

import asiankoala.ftc2021.Strategy
import com.asiankoala.koawalib.subsystem.old.MotorSubsystem
import com.asiankoala.koawalib.subsystem.old.MotorSubsystemConfig

class Turret(config: MotorSubsystemConfig) : MotorSubsystem(config) {
    companion object TurretConstants {
        private const val turretDepositDiff = 69.0
        const val turretHomeValue = 180.0
        const val blueAngle = turretHomeValue + turretDepositDiff
        const val redAngle = turretHomeValue - turretDepositDiff
        const val sharedBlueAngle = 90.0
        const val sharedRedAngle = 270.0

        fun getAngleForStrategy(strategy: Strategy): Double {
            return when(strategy) {
                Strategy.ALLIANCE_BLUE -> blueAngle
                Strategy.ALLIANCE_RED -> redAngle
                Strategy.SHARED_BLUE -> sharedBlueAngle
                Strategy.SHARED_RED -> sharedRedAngle
            }
        }
    }
}