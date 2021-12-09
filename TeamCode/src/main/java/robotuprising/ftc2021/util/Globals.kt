package robotuprising.ftc2021.util

import com.acmerobotics.dashboard.config.Config
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.OpModeType

@Config
object Globals {
    const val IS_COMP = false
    var ALLIANCE_SIDE = AllianceSide.BLUE
    var OPMODE_TYPE = OpModeType.AUTO

    const val LINKAGE_RETRACT = 0.95
    const val LINKAGE_MED = 0.75
    const val LINKAGE_EXTEND = 0.5
    const val LINKAGE_TRANSFER = 0.95
    var LINKAGE_CUSTOM = 0.75

    @JvmField var OUTTAKE_LEFT_IN = 0.08
    @JvmField var OUTTAKE_RIGHT_IN = 0.42

    @JvmField var OUTTAKE_LEFT_OUT = 0.495
    @JvmField var OUTTAKE_RIGHT_OUT = 0.02

    @JvmField var OUTTAKE_LEFT_MED = 0.20
    @JvmField var OUTTAKE_RIGHT_MED = 0.32

    const val INTAKE_PIVOT_LEFT_OUT = 0.83
    const val INTAKE_PIVOT_RIGHT_OUT = 0.02

    const val INTAKE_PIVOT_LEFT_IN = 0.10
    const val INTAKE_PIVOT_RIGHT_IN = 0.72

    const val INTAKE_IN_POWER = 1.0
    const val INTAKE_TRANSFER_POWER = -0.75
    const val INTAKE_NO_POWER = 0.0

    const val LIFT_LOW = 400
    const val LIFT_HIGH = 900

    val MASTER_MAPPINGS = listOf("FL", "FR", "BR", "BL")
    val SLAVE_MAPPINGS = listOf("intake", "liftLeft", "liftRight", "duck")
}
