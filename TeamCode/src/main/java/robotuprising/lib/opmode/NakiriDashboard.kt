package robotuprising.lib.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * FTCDashboard but better
 * @property dashboardAdapter adapter to add data to FTCDashboard packet easier
 * @property telemetryImpl reference to telemetry implementation in opmode
 *
 * example usage: NakiriDashboard["lift height"] = lift.position
 */
object NakiriDashboard {
    private var dashboardAdapter: TelemetryAdapter = TelemetryAdapter()
    private lateinit var telemetryImpl: Telemetry
    private var isUpdatingDashboard: Boolean = true

    /**
     * Initializes NakiriDashboard, must be called prior to update()
     * @param telemImpl running opmode's telemetry implementation
     * @param shouldUpdate if FTCDashboard should be updated every loop, disable during competition
     */
    fun init(telemImpl: Telemetry, shouldUpdate: Boolean) {
        telemetryImpl = telemImpl
        isUpdatingDashboard = shouldUpdate
    }

    fun fieldOverlay(): Canvas {
        return dashboardAdapter.fieldOverlay()
    }

    fun setHeader(v: String) {
        addSpace()
        addLine("------$v------")
    }

    fun addLine(v: String) {
        dashboardAdapter.addLine(v)
        telemetryImpl.addLine(v)
    }

    fun addSpace() {
        addLine(" ")
    }

    fun update() {
        if (isUpdatingDashboard) {
            FtcDashboard.getInstance().sendTelemetryPacket(dashboardAdapter)
        }
        telemetryImpl.update()
        dashboardAdapter = TelemetryAdapter()
    }

    operator fun set(k: String, v: Any) {
        telemetryImpl.addData(k, v)
        dashboardAdapter.put(k, v)
    }
}
