package robotuprising.ftc2021.hardware

import robotuprising.ftc2021.hardware.subsystems.Nakiri
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.BaseOpMode

abstract class NakiriOpMode : BaseOpMode() {
    lateinit var superstructure: Nakiri

    override fun m_init() {
        BulkDataManager.init(hardwareMap)
        NakiriDashboard.init(telemetry, true)
        superstructure = Nakiri()
        superstructure.resetRobot()
    }

    override fun m_init_loop() {
        BulkDataManager.read()
        superstructure.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

    override fun m_start() {
        // would disable camera here : TODO
    }

    override fun m_loop() {
        BulkDataManager.read()
        superstructure.update()
//        superstructure.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

    override fun m_stop() {
        superstructure.stop()
    }

    override val is_comp: Boolean
        get() = Globals.IS_COMP
}
