package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class DuckSpinner : Subsystem() {
    companion object {
        private const val MAX_POWER = 0.3
    }

    private val spinnerMotor = NakiriMotor("duckSpinner", false)

    private var changedState = false
    private var spinnerState = SpinnerStates.OFF
        set(value) {
            changedState = true
            field = value
        }

    private enum class SpinnerStates {
        ON,
        OFF
    }

    fun turnOn() {
        spinnerState = SpinnerStates.ON
    }

    fun turnOff() {
        spinnerState = SpinnerStates.OFF
    }

//    override fun init(hwMap: HardwareMap) {
//        spinnerMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        spinnerMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//    }

    override fun update() {
        spinnerMotor.power = when (spinnerState) {
            SpinnerStates.ON -> MAX_POWER
            SpinnerStates.OFF -> 0.0
        }
    }

    override fun stop() {
        spinnerMotor.power = 0.0
    }

    override fun sendDashboardPacket() {
        NakiriDashboard["spinner state"] = spinnerState
        NakiriDashboard["spinner motor power"] = spinnerMotor.power
    }
}
