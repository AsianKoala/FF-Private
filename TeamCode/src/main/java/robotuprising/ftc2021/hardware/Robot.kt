package robotuprising.ftc2021.hardware

import robotuprising.ftc2021.hardware.subsystems.Nakiri
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.system.BaseOpMode

abstract class Robot : BaseOpMode() {
    val superstructure = Nakiri

    override fun m_init() {
        superstructure.init(hardwareMap)
    }

    override fun m_init_loop() {
        superstructure.init_loop()
        superstructure.sendDashboardPacket()
    }

    override fun m_start() {
        superstructure.start()
    }

    override fun m_loop() {
        superstructure.update()
        superstructure.sendDashboardPacket()
    }

    override fun m_stop() {
        superstructure.stop()
    }
}
