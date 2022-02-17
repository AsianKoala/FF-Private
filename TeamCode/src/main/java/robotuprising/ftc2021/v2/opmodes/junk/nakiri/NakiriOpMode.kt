package robotuprising.ftc2021.v2.opmodes.junk.nakiri

import robotuprising.ftc2021.v2.subsystems.nakiri.Nakiri
import robotuprising.ftc2021.v2.manager.BulkDataManager
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.BaseOpMode

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
