package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.opmode.AllianceSide

object Globals {
    const val TELEOP_NAME = "AkemiTele"
    const val IS_COMP = false
    var ALLIANCE_SIDE = AllianceSide.BLUE
    lateinit var hwMap: HardwareMap // cringe!


    /**
     * RANDOM CONSTANTS CAUSE IM LAZY
     */

    const val LINKAGE_RETRACT = 0.5
    const val LINKAGE_MED = 0.7
    const val LINKAGE_EXTEND = 1.0

    const val OUTTAKE_LEFT_IN = 0.35
    const val OUTTAKE_RIGHT_IN = 0.25

    const val OUTTAKE_LEFT_OUT = 0.0
    const val OUTTAKE_RIGHT_OUT = 0.60

    const val OUTTAKE_LEFT_MED = (OUTTAKE_LEFT_IN + OUTTAKE_LEFT_OUT) / 2.0
    const val OUTTAKE_RIGHT_MED = (OUTTAKE_RIGHT_IN + OUTTAKE_RIGHT_OUT) / 2.0

    const val INTAKE_PIVOT_LEFT_IN = 0.88
    const val INTAKE_PIVOT_RIGHT_IN = 0.02

    const val INTAKE_PIVOT_LEFT_OUT = 0.1
    const val INTAKE_PIVOT_RIGHT_OUT = 0.75

    @JvmStatic var LIFT_LOW = 10
    @JvmStatic var LIFT_MED = 200
    @JvmStatic var LIFT_HIGH = 380
}
