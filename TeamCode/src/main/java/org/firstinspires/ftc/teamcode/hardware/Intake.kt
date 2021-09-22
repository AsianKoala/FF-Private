package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.system.Subsystem
import org.openftc.revextensions2.ExpansionHubMotor

class Intake : Subsystem() {
    private lateinit var intakeMotor: ExpansionHubMotor

    private enum class IntakeStates {
        ON,
        OFF,
        REVERSE,
    }

    private val max = 1.0
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
            setHWValues()
        }
        lastState = intakeState
    }

    override fun updateTelemetry(): HashMap<String, Any> {
        val r = HashMap<String, Any>()
        r["max"] = max
        r["intakeStates"] = intakeState
        r["power"] = power
        return r
    }

    override fun stop() {
        turnOff()
    }

    override fun setHWValues() {
        intakeMotor.power = power
    }
}
