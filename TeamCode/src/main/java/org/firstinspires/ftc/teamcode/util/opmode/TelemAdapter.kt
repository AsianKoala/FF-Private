package org.firstinspires.ftc.teamcode.util.opmode

import com.acmerobotics.dashboard.telemetry.TelemetryPacket

class TelemAdapter : TelemetryPacket() {
    val internalMap = HashMap<String, Any>()

    fun addData(k: String, v: Any) {
        internalAdd(k, v)
    }

    fun addSpace() {
        addLine(" ")
    }

    private fun internalAdd(k: String, v: Any) {
        internalMap[k] = v
        addLine("$k: $v")
    }
}
