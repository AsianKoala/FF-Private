package robotuprising.ftc2021.subsystems.wraith

import kotlin.math.absoluteValue

// degrees, inches, degrees relative to slides
data class WraithState(var turret: Double, var slide: Double, var arm: Double) {

    val asList get() = listOf(turret, slide,  arm)

    // find all data relative to the robot TODO
    // @see Constants.kt
    val slideX: Double get() = TODO()

    val slideY: Double get() = TODO()

    val slideZ: Double get() = TODO()

    val armX: Double get() = TODO()

    val armY: Double get() = TODO()

    val armZ: Double get() = TODO()

    fun isAtDesiredState(targetState: WraithState): Boolean {
        val targetStateList = targetState.asList

        asList.forEachIndexed { i, it ->
            if((targetStateList[i] - it).absoluteValue < thresholds[i]) {
                return false
            }
        }

        return true
    }

    companion object {
        const val turretDegreesThreshold = 0.5
        const val slideInchesThreshold = 0.25
        const val armDegreesThreshold = 1.0

        val thresholds = listOf(turretDegreesThreshold,  slideInchesThreshold, armDegreesThreshold)
    }
}