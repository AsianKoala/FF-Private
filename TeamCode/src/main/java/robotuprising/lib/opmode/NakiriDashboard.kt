package robotuprising.lib.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * FTCDashboard but better
 * @property telemetryAdapter adapter to add data to FTCDashboard packet easier
 * @property internalTelemetry reference to telemetry implementation in opmode
 */
object NakiriDashboard {
    private var telemetryAdapter: TelemetryAdapter = TelemetryAdapter()
    private lateinit var internalTelemetry: Telemetry
    private var isUpdatingDashboard: Boolean = false

    /**
     * Initializes NakiriDashboard, must be called prior to update()
     * @param telemImpl running opmode's telemetry implementation
     * @param shouldUpdate if FTCDashboard should be updated every loop, disable during competition
     */
    fun init(telemImpl: Telemetry, shouldUpdate: Boolean) {
        internalTelemetry = telemImpl
        isUpdatingDashboard = shouldUpdate
    }

    fun fieldOverlay(): Canvas {
        return telemetryAdapter.fieldOverlay()
    }

    fun setHeader(v: String) {
        addSpace()
        addLine("------$v------")
    }

    fun addLine(v: String) {
        telemetryAdapter.addLine(v)
        internalTelemetry.addLine(v)
    }

    fun addData(k: String, v: Any) {
        internalTelemetry.addData(k, v)
        telemetryAdapter.put(k, v)
    }

    fun addSpace() {
        telemetryAdapter.addSpace()
        internalTelemetry.addLine()
    }

    fun update() {
        if (isUpdatingDashboard) {
            FtcDashboard.getInstance().sendTelemetryPacket(telemetryAdapter)
        }
        internalTelemetry.update()
        telemetryAdapter = TelemetryAdapter()
    }

    operator fun set(k: String, v: Any) {
        internalTelemetry.addData(k, v)
        telemetryAdapter.addData(k, v)
    }
}
