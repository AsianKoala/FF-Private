package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.util.Constants
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Vector3d
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

// degrees, inches, degrees
data class WraithState(
        var turret: Double = Turret.config.homePosition,
        var slide: Double = Slide.config.homePosition,
        var arm: Double = Constants.armMountAngle,
        var outtake: Double = Constants.outtakeMountAngle,
) {

    val asList get() = listOf(turret, slide, arm)

    // find all data relative to the robot
    // TODO test if actually works
    val slideX: Double get() = -1 * cos(turret.radians) * Constants.slideTopXOffset + Constants.turretCenterXOffset

    val slideY: Double get() = -1 * sin(turret.radians) * Constants.slideTopYOffset + Constants.turretCenterYOffset

    val slideZ: Double get() = Constants.slideStages * slide * sin(Constants.slideMountAngle.radians) + Constants.turretCenterZOffset


    private val topDownArmLength = Constants.armLength * cos(arm.radians)

    val armX: Double get() = slideX + topDownArmLength * cos(turret.radians)

    val armY: Double get() = slideY + topDownArmLength * sin(turret.radians)

    val armZ: Double get() = slideZ + Constants.armLength * sin(arm.radians)


    val slide3dPosition get() = Vector3d(slideX, slideY, slideZ)
    val arm3dPosition get() = Vector3d(armX, armY, armZ)

    fun isAtDesiredState(targetState: WraithState): Boolean {
        val targetStateList = targetState.asList

        asList.forEachIndexed { i, it ->
            if ((targetStateList[i] - it).absoluteValue < thresholds[i]) {
                return false
            }
        }

        return true
    }

    companion object {
        private val turretEpsilon = Turret.config.positionEpsilon
        private val slideEpsilon = Slide.config.positionEpsilon
        private val armEpsilon = 1.0
        private val outtakeEpsilon = 1.0

        private val thresholds = listOf(turretEpsilon, slideEpsilon, armEpsilon, outtakeEpsilon)
    }
}
