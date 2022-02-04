package robotuprising.ftc2021.subsystems.osiris

import com.acmerobotics.roadrunner.util.epsilonEquals
import robotuprising.ftc2021.subsystems.osiris.hardware.Slides
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret
import robotuprising.ftc2021.util.Constants

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
                arm epsilonEquals  targetState.arm &&
                outtake epsilonEquals  targetState.outtake
    }
}
