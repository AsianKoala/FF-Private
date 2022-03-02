package neil.koawalib.control

import neil.koawalib.control.feedforward.DisableFeedforward
import neil.koawalib.control.feedforward.Feedforward

data class PIDFConfig(
        val kP: Double = 0.0,
        val kI: Double = 0.0,
        val kD: Double = 0.0,

        val feedforward: Feedforward = DisableFeedforward(),

        val positionEpsilon: Double = 1.0,
        val homePositionToDisable: Double = Double.NaN,
        val ticksPerUnit: Double = 1.0
)