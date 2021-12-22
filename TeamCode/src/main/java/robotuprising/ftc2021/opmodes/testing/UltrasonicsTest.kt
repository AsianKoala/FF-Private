package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.subsystems.nakiri.Ultrasonics
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.BaseOpMode

@TeleOp
@Disabled
class UltrasonicsTest : BaseOpMode() {
    private lateinit var ultrasonics: Ultrasonics

    override fun m_init() {
        BulkDataManager.init(hardwareMap)
        NakiriDashboard.init(telemetry, true)
        ultrasonics = Ultrasonics()
    }

    override fun m_start() {
        ultrasonics.startReading()
    }

    override fun m_loop() {
        BulkDataManager.read()
        ultrasonics.update()
        ultrasonics.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

}