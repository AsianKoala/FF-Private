package neil.koawalib.subsystem

import neil.koawalib.hardware.motor.KMotorEx
import neil.koawalib.hardware.sensor.KLimitSwitch

abstract class ZeroableDeviceSubsystem(
       protected val motor: KMotorEx,
       private val limitSwitch: KLimitSwitch
) : DeviceSubsystem() {
    protected abstract val zeroPosition: Double

    private var zeroing = false

    fun startZeroAttempt() {
        zeroing = true
    }

    fun endZeroAttempt() {
        zeroing = false
    }

    override fun periodic() {
        if(zeroing) {
            if(limitSwitch.invokeBoolean())  {
                motor.zero(zeroPosition)
            }
        }
    }
}