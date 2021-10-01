package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.system.Subsystem
import robotuprising.lib.opmode.AkemiDashboard
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.hardware.Accuracy
import robotuprising.lib.hardware.Status

object Intake : Subsystem() {
    private lateinit var intakeMotor: ExpansionHubMotor

    private enum class IntakeStates {
        ON,
        OFF,
        REVERSE,
    }

    private var max = 1.0
    private var power = 0.0

    private var lastState = IntakeStates.OFF
    private var intakeState = IntakeStates.OFF

    fun turnOn() {
        intakeState = IntakeStates.ON
    }

    fun turnOff() {
        intakeState = IntakeStates.OFF
    }

    fun reverse() {
        intakeState = IntakeStates.REVERSE
    }

    fun setMax(max: Double) {
        Intake.max = max
    }

    override fun init(hwMap: HardwareMap) {
        intakeMotor = hwMap[ExpansionHubMotor::class.java, "intake"]
    }

    override fun update() {
        power = when (intakeState) {
            IntakeStates.ON -> max
            IntakeStates.OFF -> 0.0
            IntakeStates.REVERSE -> -max
        }

        if (lastState != intakeState) {
            setHWValues(power)
        }
        lastState = intakeState
    }

    override fun sendDashboardPacket() {
        val r = HashMap<String, Any>()
        r["max"] = max
        r["intakeStates"] = intakeState
        r["power"] = power
        AkemiDashboard.addAll(r)
    }

    override fun stop() {
        turnOff()
    }

    override var status: Status = Status.ALIVE
    override var acc: Accuracy = Accuracy.LOW

    private fun setHWValues(powerV: Double) {
        intakeMotor.power = powerV
    }
}
