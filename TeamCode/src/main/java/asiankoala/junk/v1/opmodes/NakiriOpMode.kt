package asiankoala.junk.v1.opmodes

import asiankoala.junk.v1.subsystems.Nakiri
import asiankoala.junk.v2.lib.opmode.NakiriDashboard
import asiankoala.junk.v2.lib.system.BaseOpMode
import asiankoala.junk.v2.manager.BulkDataManager

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
