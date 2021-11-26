package robotuprising.ftc2021.util

import robotuprising.lib.opmode.AllianceSide

object Globals {
    const val IS_COMP = false
    var ALLIANCE_SIDE = AllianceSide.BLUE

    const val LINKAGE_RETRACT = 1.0
    const val LINKAGE_MED = 0.75
    const val LINKAGE_EXTEND = 0.5
    const val LINKAGE_TRANSFER = 0.95
    var LINKAGE_CUSTOM = 0.75

    const val OUTTAKE_LEFT_IN = 0.05
    const val OUTTAKE_RIGHT_IN = 0.42

    const val OUTTAKE_LEFT_OUT = 0.50
    const val OUTTAKE_RIGHT_OUT = 0.02

    const val OUTTAKE_LEFT_MED = 0.20
    const val OUTTAKE_RIGHT_MED = 0.32

    const val INTAKE_PIVOT_LEFT_OUT = 0.83
    const val INTAKE_PIVOT_RIGHT_OUT = 0.02

    const val INTAKE_PIVOT_LEFT_IN = 0.10
    const val INTAKE_PIVOT_RIGHT_IN = 0.72

    const val INTAKE_IN_POWER = 1.0
    const val INTAKE_TRANSFER_POWER = -0.75
    const val INTAKE_NO_POWER = 0.0

    const val LIFT_LOW = 60
    const val LIFT_HIGH = 400

    val MASTER_MAPPINGS = listOf("FL", "FR", "BR", "BL")
    val SLAVE_MAPPINGS = listOf("intake", "liftLeft", "liftRight", "duck")
}
