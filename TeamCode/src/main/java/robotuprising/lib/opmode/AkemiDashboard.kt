package robotuprising.lib.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * FTCDashboard but better
 * @property telemAdapter adapter to add data to FTCDashboard packet easier
 * @property internalTelemetry reference to telemetry implementation in opmode
 */
object AkemiDashboard {
    private var telemAdapter: TelemAdapter = TelemAdapter()
    private lateinit var internalTelemetry: Telemetry

    fun init(telemImpl: Telemetry) {
        internalTelemetry = telemImpl
    }

    fun addAll(other: HashMap<String, Any>) {
        other.forEach { set(it.key, it.value) }
        addSpace()
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

    operator fun set(k: String, v: Any) {
        internalTelemetry.addData(k, v)
        telemAdapter.addData(k, v)
    }

}
