package org.firstinspires.ftc.teamcode.util.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl

class AkemiTelemetry(opMode: OpMode) {
    private var telemAdapter: TelemAdapter = TelemAdapter()
    private val telemetry: TelemetryImpl = TelemetryImpl(opMode)

    fun addData(key: String, `val`: Any) {
        telemetry.addData(key, `val`)
        telemAdapter.addData(key, `val`)
    }

    fun addAll(other: AkemiTelemetry) {
        other.telemAdapter.internalMap.forEach { addData(it.key, it.value) }
    }

    fun addSpace() {
        telemetry.addLine()
        telemAdapter.addSpace()
    }

    fun fieldOverlay(): Canvas {
        return telemAdapter.fieldOverlay()
    }

    fun update() {
        telemetry.update()
        FtcDashboard.getInstance().sendTelemetryPacket(telemAdapter)
        telemAdapter = TelemAdapter()
    }
}
