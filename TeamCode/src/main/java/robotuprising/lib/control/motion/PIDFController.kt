package robotuprising.lib.control.motion

import robotuprising.lib.util.PrimitiveExtensions.d
import kotlin.math.abs
import kotlin.math.sign

class PIDFController(private val kp: Double, private val ki: Double, private val kd: Double, private val kv: Double=0.d, private val ka: Double=0.d, private val ks: Double=0.d) {

    private var target = Double.NaN
    private var targetVel = Double.NaN
    private var targetAccel = Double.NaN

    private var errorsum = Double.NaN
    private var lasterror = Double.NaN

    private var lastupdate: Long = Long.MIN_VALUE

    private var bounded = false
    private var upperbound = 1.0
    private var lowerbound = -1.0

    // public members
    val getCoeffs get() = mutableListOf(kp, ki, kd, kv, ka, ks)

    fun setTargets(t: Double, tv: Double, ta: Double) {
        target = t
        targetVel = tv
        targetAccel = ta
    }

    fun setBounds(upper: Double, lower: Double) {
        bounded = true
        upperbound = upper
        lowerbound = lower
    }

    fun unbound() {
        bounded = false
    }

    fun reset() {
        target = Double.NaN
    }

    fun setErrorSum(error: Double) {
        errorsum = error
    }

    fun update(position: Double, refvel: Double?): Double {
        val error = target - position
        val timestamp = System.currentTimeMillis()

        return if (lastupdate == Long.MIN_VALUE) {
            lasterror = error
            lastupdate = timestamp
            0.0
        } else {
            val dt = timestamp - lastupdate
            val deriv = (error - lasterror) / dt
            errorsum += error * dt

            lasterror = error
            lastupdate = timestamp

            val baseoutput = kp * error + ki * errorsum + kd * deriv + (refvel ?: targetVel) * kv + targetAccel * ka
            val output = if (abs(baseoutput) < 1E-6) 0.0 else baseoutput + baseoutput.sign * ks

            if (bounded) {
                when {
                    output < lowerbound -> lowerbound
                    output > upperbound -> upperbound
                    else -> output
                }
            } else {
                output
            }
        }
    }
}
