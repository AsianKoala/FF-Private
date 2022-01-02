package robotuprising.ftc2021.opmodes.wraith

import robotuprising.ftc2021.subsystems.wraith.Wraith
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.lib.opmode.WraithDashboard
import robotuprising.lib.system.BaseOpMode

class WraithOpMode : BaseOpMode() {
    lateinit var wraith: Wraith

    override fun mInit() {
        BulkDataManager.init(hardwareMap)
        WraithDashboard.init(telemetry, true)

        wraith = Wraith()
        wraith.reset()
    }

    override fun mInitLoop() {
        BulkDataManager.read()
        wraith.periodic()
    }

    override fun mLoop() {
        BulkDataManager.read()
        wraith.periodic()
    }

    override fun mStop() {
        wraith.reset()
    }


}