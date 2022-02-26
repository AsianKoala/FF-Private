package robotuprising.koawalib.gamepad

import robotuprising.koawalib.util.interfaces.KDouble
import robotuprising.koawalib.util.interfaces.Periodic
import kotlin.math.abs


open class AxisBase(
        var d: () -> Double,
        private var triggerThreshold: Double = DEFAULT_TRIGGER_THRESHOLD)
    : ButtonBase( { abs(d.invoke()) >= triggerThreshold }), KDouble, Periodic {

    /** Set threshold
     * @param threshold the new threshold
     */
    open fun setTriggerThreshold(threshold: Double): AxisBase {
        triggerThreshold = threshold
        return this
    }

    /** Returns the double from the axis
     * @return The double
     */

    override fun invokeDouble(): Double {
        if (isDisabled) return 0.0
        return if (inverted) -d.invoke() else d.invoke()
    }

    companion object {
        const val DEFAULT_TRIGGER_THRESHOLD = 0.05
    }
}
