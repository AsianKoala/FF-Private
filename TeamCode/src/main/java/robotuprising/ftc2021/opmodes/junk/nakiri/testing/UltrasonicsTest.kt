package robotuprising.ftc2021.opmodes.junk.nakiri.testing

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.subsystems.nakiri.Ultrasonics
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.BaseOpMode

@TeleOp
@Disabled
class UltrasonicsTest : BaseOpMode() {
    private lateinit var ultrasonics: Ultrasonics

    override fun mInit() {
        BulkDataManager.init(hardwareMap)
        NakiriDashboard.init(telemetry, true)
        ultrasonics = Ultrasonics()
    }

    override fun mStart() {
        ultrasonics.startReading()
    }

    override fun mLoop() {
        BulkDataManager.read()
        ultrasonics.update()
        ultrasonics.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

}