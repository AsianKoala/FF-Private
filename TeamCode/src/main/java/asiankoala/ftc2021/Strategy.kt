package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.Arm
import asiankoala.ftc2021.subsystems.Outtake
import asiankoala.ftc2021.subsystems.Slides
import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger

class Strategy(val alliance: Alliance = Alliance.BLUE) {
    var strat = alliance.decide(Strats.ALLIANCE_BLUE, Strats.ALLIANCE_RED)

    var shouldExtendFurther: Boolean = false
    val isAttackingOtherCrater: Boolean
        get() {
            return if(alliance == Alliance.BLUE) {
                strat == Strats.SHARED_RED
            } else {
                strat == Strats.SHARED_BLUE
            }
        }

    val isAlliance: Boolean
        get() = (strat == Strats.ALLIANCE_BLUE || strat == Strats.ALLIANCE_RED)

    val isShared: Boolean
        get() = !isAlliance

    val isExtendingImmediately: Boolean
        get() = isShared && !isAttackingOtherCrater

    fun getTurretAngle(): Double {
        return when(strat) {
            Strats.ALLIANCE_BLUE -> Turret.blueAngle
            Strats.ALLIANCE_RED -> Turret.redAngle
            Strats.SHARED_BLUE -> Turret.sharedBlueAngle
            Strats.SHARED_RED -> Turret.sharedRedAngle
        }
    }
    
    fun getSlideInches(): Double {
        return when {
            shouldExtendFurther -> Slides.sharedExtInches
            isAlliance -> Slides.depositHighInches
            else -> Slides.sharedInches
        }
    }

    fun getArmPosition(): Double {
        return when(isShared) {
            true -> Arm.armSharedPosition
            false -> Arm.armHighPosition
        }
    }

    fun getOuttakePosition(): Double {
        return when(isShared) {
            true -> Outtake.outtakeSharedPosition
            false -> Outtake.outtakeHighPosition
        }
    }

    fun log() {
        Logger.addTelemetryData("strat", strat)
        Logger.addTelemetryData("should extend further", shouldExtendFurther)
        Logger.addTelemetryData("is attacking other crater", isAttackingOtherCrater)
        Logger.addTelemetryData("is extending immediately", isExtendingImmediately)
        Logger.addTelemetryData("turret target angle", getTurretAngle())
        Logger.addTelemetryData("slide target inches", getSlideInches())
    }
}

