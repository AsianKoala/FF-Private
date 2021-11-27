package robotuprising.ftc2021.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

// todo change to velocity control
class DuckSpinner : Subsystem {
    private val spinnerMotor = NakiriMotor("duck", false)

    private var spinnerState = SpinnerStates.OFF
    private enum class SpinnerStates(val power: Double) {
        ON(0.2),
        OFF(0.0),
        REVERSE(-0.2)
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
        NakiriDashboard.setHeader("duck spinner")
        NakiriDashboard["state"] = spinnerState

        if (debugging) {
            NakiriDashboard["motor power"] = spinnerMotor.power
            spinnerMotor.sendDataToDashboard()
        }
    }
}
