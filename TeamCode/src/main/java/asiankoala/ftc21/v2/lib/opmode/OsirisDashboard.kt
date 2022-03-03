package asiankoala.ftc21.v2.lib.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import org.firstinspires.ftc.robotcore.external.Telemetry

/**
 * FTCDashboard but better
 * @property dashboardAdapter adapter to add data to FTCDashboard packet easier
 * @property telemetryImpl reference to telemetry implementation in opmode
 *
 * example usage: WraithDashboard["lift height"] = lift.position
 */
object OsirisDashboard {
    private var dashboardAdapter: TelemetryAdapter = TelemetryAdapter()
    private lateinit var telemetryImpl: Telemetry
    private var isUpdatingDashboard: Boolean = false

    /**
     * Initializes WraithDashboard, must be called prior to update()
     * @param telemImpl running opmode's telemetry implementation
     * @param shouldUpdate if FTCDashboard should be updated every loop, disable during competition
     */
    fun init(telemImpl: Telemetry, shouldUpdate: Boolean) {
        telemetryImpl = telemImpl
        isUpdatingDashboard = shouldUpdate
    }

    fun fieldOverlay(): Canvas? {
        return if(isUpdatingDashboard) {
            dashboardAdapter.fieldOverlay()
        } else {
            null
        }
    }

    fun setHeader(v: String) {
        addSpace()
        addLine("------$v------")
    }

    fun addLine(v: String) {
        if(isUpdatingDashboard) {
            dashboardAdapter.addLine(v)
        }

        telemetryImpl.addLine(v)
    }

    fun addLine() {
        addLine("")
    }

    fun addSpace() {
        addLine(" ")
    }

    fun update() {
        if (isUpdatingDashboard) {
            FtcDashboard.getInstance().sendTelemetryPacket(dashboardAdapter)
            dashboardAdapter = TelemetryAdapter()
        }
        telemetryImpl.update()
        telemetryImpl.clearAll()
    }

    operator fun set(k: String, v: Any) {
        telemetryImpl.addData(k, v)

        if(isUpdatingDashboard) {
            dashboardAdapter.put(k, v)
        }
    }
}
