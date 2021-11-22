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
    const val LINKAGE_MED = 0.75
    const val LINKAGE_EXTEND = 0.5
    var LINKAGE_CUSTOM = 0.75

    const val OUTTAKE_LEFT_IN = 0.50
    const val OUTTAKE_RIGHT_IN = 0.10

    const val OUTTAKE_LEFT_OUT = 0.0
    const val OUTTAKE_RIGHT_OUT = 0.60

    const val OUTTAKE_LEFT_MED = 0.35 // 0.30 / 0.30
    const val OUTTAKE_RIGHT_MED = 0.25

    const val INTAKE_PIVOT_LEFT_OUT = 0.83
    const val INTAKE_PIVOT_RIGHT_OUT = 0.02

    const val INTAKE_PIVOT_LEFT_IN = 0.07
    const val INTAKE_PIVOT_RIGHT_IN = 0.76

    const val INTAKE_IN_POWER = 1.0
    const val INTAKE_TRANSFER_POWER = -0.75
    const val INTAKE_NO_POWER = 0.0

    const val LIFT_LOW = 45
    const val LIFT_HIGH = 400

    val PID_COEFFS = PIDCoefficients(0.05, 0.0, 0.0)

    val MASTER_MAPPINGS = listOf("FL", "FR", "BR", "BL")
    val SLAVE_MAPPINGS = listOf("intake", "liftLeft", "liftRight", "duck")

    /*

     cube:
     1.7k -> 9.5k red
     3k -> 11k green
     2k -> 4k blue

     ball:
     1.7k -> 2.6k red
     3.2k -> 6.5k green
     2.3k -> 3.3k blue

     */
    val NONE_RGB = Triple(1520, 2790, 2027)
    val BALL_RGB = Triple(2235, 4018, 2844)
    val CUBE_RGB = Triple(3159, 4817, 2579)
}
