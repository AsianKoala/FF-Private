package asiankoala.junk.v2.subsystems

import asiankoala.junk.v2.util.Constants
import com.acmerobotics.roadrunner.util.epsilonEquals

// degrees, inches, degrees
data class OsirisState(
    var turret: Double = Constants.turretHomeValue,
    var slide: Double = Constants.slideHomeValue,
    var arm: Double = Constants.armHomePosition,
    var outtake: Double = Constants.outtakeHomePosition,
) {

    fun isAtDesiredState(targetState: OsirisState): Boolean {
        return turret epsilonEquals targetState.turret &&
            slide epsilonEquals targetState.slide &&
            arm epsilonEquals targetState.arm &&
            outtake epsilonEquals targetState.outtake
    }
}
