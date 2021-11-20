package robotuprising.ftc2021.util

import com.acmerobotics.roadrunner.control.PIDCoefficients
import org.firstinspires.ftc.robotcore.external.Telemetry
import robotuprising.lib.opmode.AllianceSide

object Globals {
    const val TELEOP_NAME = "AkemiTele"
    const val IS_COMP = false
    var ALLIANCE_SIDE = AllianceSide.BLUE
    lateinit var telemetry: Telemetry


    /**
     * RANDOM CONSTANTS CAUSE IM LAZY
     */

    const val LINKAGE_RETRACT = 1.0
    const val LINKAGE_MED = 0.7
    const val LINKAGE_EXTEND = 0.5

    const val OUTTAKE_LEFT_IN = 0.35
    const val OUTTAKE_RIGHT_IN = 0.25

    const val OUTTAKE_LEFT_OUT = 0.0
    const val OUTTAKE_RIGHT_OUT = 0.60

    const val OUTTAKE_LEFT_MED = (OUTTAKE_LEFT_IN + OUTTAKE_LEFT_OUT) / 2.0
    const val OUTTAKE_RIGHT_MED = (OUTTAKE_RIGHT_IN + OUTTAKE_RIGHT_OUT) / 2.0

    const val INTAKE_PIVOT_LEFT_OUT = 0.85
    const val INTAKE_PIVOT_RIGHT_OUT = 0.00

    const val INTAKE_PIVOT_LEFT_IN = 0.07
    const val INTAKE_PIVOT_RIGHT_IN = 0.78

    const val INTAKE_IN_POWER = 1.0
    const val INTAKE_TRANSFER_POWER = -0.75
    const val INTAKE_NO_POWER = 0.0

    const val LIFT_LOW = 60
    const val LIFT_HIGH = 400

    val PID_COEFFS = PIDCoefficients(0.05, 0.0, 0.0)

    val MASTER_MAPPINGS = listOf("FL", "FR", "BR", "BL")
    val SLAVE_MAPPINGS = listOf("intake", "liftLeft", "liftRight", "duck")
}
