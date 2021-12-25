package robotuprising.ftc2021.opmodes.wraith

import robotuprising.ftc2021.subsystems.wraith.Wraith
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.lib.opmode.WraithDashboard
import robotuprising.lib.system.BaseOpMode

class WraithOpMode : BaseOpMode() {
    lateinit var wraith: Wraith

    override fun m_init() {
        BulkDataManager.init(hardwareMap)
        WraithDashboard.init(telemetry, true)

        wraith = Wraith()
        wraith.reset()
    }

    override fun m_init_loop() {
        BulkDataManager.read()
        wraith.periodic()
    }

    override fun m_loop() {
        BulkDataManager.read()
        wraith.periodic()
    }

    override fun m_stop() {
        wraith.reset()
    }


}