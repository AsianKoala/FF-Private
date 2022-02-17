package robotuprising.ftc2021.v2.subsystems.nakiri

import robotuprising.ftc2021.v2.hardware.nakiri.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class DuckSpinner : Subsystem {
    private val spinnerMotor = NakiriMotor("duck", false).brake.openLoopControl

    var velocity = 0.0

    private var spinnerState = SpinnerStates.OFF
    private enum class SpinnerStates(val power: Double) {
        ON(0.25),
        OFF(0.0),
        REVERSE(-0.25)
    }

    fun turnOn() {
        spinnerState = SpinnerStates.ON
    }

    fun turnOff() {
        spinnerState = SpinnerStates.OFF
    }

    fun reverse() {
        spinnerState = SpinnerStates.REVERSE
    }

    override fun update() {
        spinnerMotor.power = spinnerState.power
    }

    override fun reset() {
        spinnerMotor.power = 0.0
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        if (debugging) {
            NakiriDashboard["motor power"] = spinnerMotor.power
            spinnerMotor.sendDataToDashboard()
        }
    }
}
