package robotuprising.lib.control.motion

import kotlin.math.abs
import kotlin.math.sign

class PIDFController(private val coeffs: PIDFCoeffs) {

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
    val getCoeffs get() = coeffs

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
        targetVel = Double.NaN
        targetAccel = Double.NaN

        errorsum = Double.NaN
        lasterror = Double.NaN

        lastupdate = Long.MIN_VALUE
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

            val rawOutput = coeffs.kp * error + coeffs.ki * errorsum + coeffs.kd * deriv + (refvel ?: targetVel) * coeffs.kv + targetAccel * coeffs.ka
            val output = if (abs(rawOutput) < 1E-6) 0.0 else rawOutput + rawOutput.sign * coeffs.ks

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
