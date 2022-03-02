package neil.ftc21.v2.opmodes.junk.nakiri

import neil.ftc21.v2.subsystems.nakiri.Nakiri
import neil.ftc21.v2.manager.BulkDataManager
import neil.lib.opmode.NakiriDashboard
import neil.lib.system.BaseOpMode

abstract class NakiriOpMode : BaseOpMode() {
    lateinit var nakiri: Nakiri

    override fun mInit() {
        BulkDataManager.init(hardwareMap)
        NakiriDashboard.init(telemetry, false)

        nakiri = Nakiri()
        nakiri.reset()
    }

    override fun mInitLoop() {
        BulkDataManager.read()
        nakiri.sendDashboardPacket(false)
    }

    override fun mLoop() {
        BulkDataManager.read()
        nakiri.update()
        nakiri.sendDashboardPacket(false)
    }

    override fun mStop() {
        nakiri.reset()
    }
}
