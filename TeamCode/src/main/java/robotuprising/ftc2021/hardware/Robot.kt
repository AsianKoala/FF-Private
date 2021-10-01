package robotuprising.ftc2021.hardware

import robotuprising.ftc2021.hardware.subsystems.Akemi
import robotuprising.lib.system.BaseOpMode

abstract class Robot: BaseOpMode() {
    private val superstructure = Akemi

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
