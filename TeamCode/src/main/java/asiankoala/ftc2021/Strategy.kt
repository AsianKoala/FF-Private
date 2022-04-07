package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.Arm
import asiankoala.ftc2021.subsystems.Slides
import asiankoala.ftc2021.subsystems.Turret

enum class Strategy {
    ALLIANCE_BLUE,
    ALLIANCE_RED,
    SHARED_BLUE,
    SHARED_RED;
    
    fun getTurretAngle(): Double {
        return when(this) {
            ALLIANCE_BLUE -> Turret.blueAngle
            ALLIANCE_RED -> Turret.redAngle
            SHARED_BLUE -> Turret.sharedBlueAngle
            SHARED_RED -> Turret.sharedRedAngle
        }
    }
    
    fun getSlideInches(): Double {
        return when(this) {
            ALLIANCE_BLUE -> Slides.depositHighInches
            ALLIANCE_RED -> Slides.depositHighInches
            SHARED_BLUE -> Slides.sharedInches
            SHARED_RED -> Slides.sharedInches
        }
    }

    fun getArmPosition(): Double {
        return when(this) {
            ALLIANCE_BLUE -> Arm.armHighPosition
            ALLIANCE_RED -> Arm.armHighPosition
            SHARED_BLUE -> Arm.armSharedPosition
            SHARED_RED -> Arm.armSharedPosition
        }
    }
}