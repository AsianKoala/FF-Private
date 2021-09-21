package org.firstinspires.ftc.teamcode.util.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * @param telemImpl reference to telemetry implementation in opmode
 */
class AkemiTelemetry(telemImpl: Telemetry) {
    private var telemAdapter: TelemAdapter = TelemAdapter()
    private val internalTelemetry: Telemetry = telemImpl

    fun addData(key: String, `val`: Any) {
        internalTelemetry.addData(key, `val`)
        telemAdapter.addData(key, `val`)
    }

    fun addAll(other: HashMap<String, Any>) {
        other.forEach { addData(it.key, it.value) }
    }

    fun addSpace() {
        internalTelemetry.addLine()
        telemAdapter.addSpace()
    }

    fun fieldOverlay(): Canvas {
        return telemAdapter.fieldOverlay()
    }

    fun update() {
        internalTelemetry.update()
        FtcDashboard.getInstance().sendTelemetryPacket(telemAdapter)
        telemAdapter = TelemAdapter()
    }
}
