package robotuprising.lib.opmode

import com.acmerobotics.dashboard.telemetry.TelemetryPacket

class TelemetryAdapter : TelemetryPacket() {
    fun addData(k: String, v: Any) {
        addLine("$k: $v")
    }

    fun addSpace() {
        addLine(" ")
    }
}
