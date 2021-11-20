package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class DuckSpinner : Subsystem() {
    private val spinnerMotor = NakiriMotor("duck", false)

    private var spinnerState = SpinnerStates.OFF
    private enum class SpinnerStates(val power: Double) {
        ON(0.2),
        OFF(0.0)
    }

    fun turnOn() {
        spinnerState = SpinnerStates.ON
    }

    fun turnOff() {
        spinnerState = SpinnerStates.OFF
    }

    override fun update() {
        spinnerMotor.power = spinnerState.power
    }

    override fun stop() {
        spinnerMotor.power = 0.0
    }

    override fun sendDashboardPacket() {
        NakiriDashboard["spinner state"] = spinnerState
        NakiriDashboard["spinner motor power"] = spinnerMotor.power
    }
}
