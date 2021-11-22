package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotorFactory
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

// todo change to velocity control
class DuckSpinner : Subsystem {
    private val spinnerMotor = NakiriMotorFactory.name("duck").slave.float.velocityControl.create

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

    override fun stop() {
        spinnerMotor.power = 0.0
    }

    override fun sendDashboardPacket() {
        NakiriDashboard["spinner state"] = spinnerState
        NakiriDashboard["spinner motor power"] = spinnerMotor.power
    }
}
