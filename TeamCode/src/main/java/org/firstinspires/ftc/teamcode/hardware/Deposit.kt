package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.system.Subsystem
import org.openftc.revextensions2.ExpansionHubServo

class Deposit : Subsystem() {
    private lateinit var testServo: ExpansionHubServo

    override fun init(hwMap: HardwareMap) {
        testServo = hwMap[ExpansionHubServo::class.java, "deposit"]
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun updateTelemetry(): HashMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun setHWValues() {
        TODO("Not yet implemented")
    }
}
