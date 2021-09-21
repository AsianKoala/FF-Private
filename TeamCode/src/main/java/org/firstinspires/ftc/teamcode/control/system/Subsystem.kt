package org.firstinspires.ftc.teamcode.control.system

import org.openftc.revextensions2.ExpansionHubMotor

abstract class Subsystem {
    abstract fun init(vararg motors: ExpansionHubMotor)
    abstract fun update()
    abstract fun updateTelemetry(): HashMap<String, Any>
    abstract fun stop()
}
