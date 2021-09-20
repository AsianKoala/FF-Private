package org.firstinspires.ftc.teamcode.control.system

import org.firstinspires.ftc.teamcode.util.opmode.AkemiTelemetry

abstract class Subsystem {
    abstract fun update()
    abstract fun updateTelemetry(): AkemiTelemetry
    abstract fun stop()
}