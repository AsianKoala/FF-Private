package robotuprising.ftc2021.hardware

import com.acmerobotics.dashboard.config.Config
import robotuprising.lib.util.Extensions.d

@Config
object Tunables {
    @JvmField var INTAKE_PIVOT_REST: Double = 1.d
    @JvmField var INTAKE_PIVOT_OUT: Double = 0.d

    @JvmField var LIFT_MAX: Int = Int.MAX_VALUE
    @JvmField var LIFT_MIN: Int = Int.MIN_VALUE
    @JvmField var LIFT_MAX_VEL: Double = Double.NaN
    @JvmField var LIFT_MAX_ACCEL: Double = Double.NaN

    @JvmField var LIFT_COEFF_KP: Double = 0.d
    @JvmField var LIFT_COEFF_KI: Double = 0.d
    @JvmField var LIFT_COEFF_KD: Double = 0.d
    @JvmField var LIFT_COEFF_KV: Double = 0.d
    @JvmField var LIFT_COEFF_KA: Double = 0.d
    @JvmField var LIFT_COEFF_KS: Double = 0.d
}
