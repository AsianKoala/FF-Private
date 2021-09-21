package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.control.system.HardwareManager
import org.firstinspires.ftc.teamcode.control.system.Subsystem
import org.openftc.revextensions2.ExpansionHubMotor

class AkemiHWMImpl(subsystems: Subsystem) : HardwareManager(subsystems) {
    override fun setupMapping(hwMap: HardwareMap) {
        subsystems.forEach {
            when (it) {
                is Homura -> {
                    it.init(
                        hwMap[ExpansionHubMotor::class.java, "FL"],
                        hwMap[ExpansionHubMotor::class.java, "BL"],
                        hwMap[ExpansionHubMotor::class.java, "FR"],
                        hwMap[ExpansionHubMotor::class.java, "BR"]
                    )
                }
            }
        }
    }
}
