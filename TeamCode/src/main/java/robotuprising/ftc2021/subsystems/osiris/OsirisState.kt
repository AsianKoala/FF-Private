package robotuprising.ftc2021.subsystems.osiris

import com.acmerobotics.roadrunner.util.epsilonEquals
import robotuprising.ftc2021.subsystems.osiris.hardware.Slide
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret
import robotuprising.ftc2021.util.Constants

// degrees, inches, degrees
data class OsirisState(
        var turret: Double = Turret.config.homePosition,
        var slide: Double = Slide.config.homePosition,
        var arm: Double = Constants.armHomePosition,
        var outtake: Double = Constants.outtakeHomePosition,
) {

//    // find all data relative to the robot
//    // TODO test if actually works
//    val slideX: Double get() = -1 * cos(turret.radians) * Constants.slideTopXOffset + Constants.turretCenterXOffset
//
//    val slideY: Double get() = -1 * sin(turret.radians) * Constants.slideTopYOffset + Constants.turretCenterYOffset
//
//    val slideZ: Double get() = Constants.slideStages * slide * sin(Constants.slideMountAngle.radians) + Constants.turretCenterZOffset
//
//
//    private val topDownArmLength = Constants.armLength * cos(arm.radians)
//
//    val armX: Double get() = slideX + topDownArmLength * cos(turret.radians)
//
//    val armY: Double get() = slideY + topDownArmLength * sin(turret.radians)
//
//    val armZ: Double get() = slideZ + Constants.armLength * sin(arm.radians)
//
//
//    val slide3dPosition get() = Vector3d(slideX, slideY, slideZ)
//    val arm3dPosition get() = Vector3d(armX, armY, armZ)

    fun isAtDesiredState(targetState: OsirisState): Boolean {
//        val targetStateList = targetState.asList
//
//        asList.forEachIndexed { i, it ->
//            if ((targetStateList[i] - it).absoluteValue < thresholds[i]) {
//                return false
//            }
//        }
//
//        return true

        return turret epsilonEquals targetState.turret &&
                slide epsilonEquals targetState.slide &&
                arm epsilonEquals  targetState.arm &&
                outtake epsilonEquals  targetState.outtake
    }

//    companion object {
//        private val turretEpsilon = Turret.config.positionEpsilon
//        private val slideEpsilon = Slide.config.positionEpsilon
//        private val armEpsilon = 1.0
//        private val outtakeEpsilon = 1.0
//
//        private val thresholds = listOf(turretEpsilon, slideEpsilon, armEpsilon, outtakeEpsilon)
//    }
}
