package robotuprising.ftc2021.subsystems

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

// todo change to velocity control
class DuckSpinner : Subsystem {
    private val spinnerMotor = NakiriMotor("duck", false).brake.openLoopControl

    companion object {
        @JvmStatic var duckCoeffs = PIDCoefficients(0.001, 0.0, 0.0)
        @JvmStatic var targetVelocity = 0.1 // m/s^2
    }

    val controller = PIDFController(duckCoeffs)
    var velocity = 0.0

    private var spinnerState = SpinnerStates.OFF
    private enum class SpinnerStates(val power: Double) {
        ON(0.2),
        OFF(0.0),
        REVERSE(-0.2)
    }

    fun turnOn() {
        spinnerState = SpinnerStates.ON
        controller.reset()
        controller.targetPosition = targetVelocity
    }

    fun turnOff() {
        spinnerState = SpinnerStates.OFF
        controller.reset()
        velocity = 0.0
    }

    fun reverse() {
        spinnerState = SpinnerStates.REVERSE
        controller.reset()
        controller.targetPosition = -targetVelocity
    }

    override fun update() {
        if(spinnerState != SpinnerStates.OFF) {
            velocity = spinnerMotor.velocity
            spinnerMotor.power = controller.update(velocity)
        }
    }

    override fun reset() {
        spinnerMotor.power = 0.0
    }

    override fun sendDashboardPacket(debugging: Boolean) {
//        NakiriDashboard.setHeader("duck spinner")
//        NakiriDashboard["state"] = spinnerState
//        NakiriDashboard["last read velocity"] = velocity
//        NakiriDashboard["target velocity"] = targetVelocity

        if (debugging) {
            NakiriDashboard["motor power"] = spinnerMotor.power
            spinnerMotor.sendDataToDashboard()
        }
    }
}
