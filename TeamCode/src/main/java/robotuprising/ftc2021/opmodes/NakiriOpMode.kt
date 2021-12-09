package robotuprising.ftc2021.opmodes

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import robotuprising.ftc2021.subsystems.Nakiri
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.opmode.OpModeType
import robotuprising.lib.system.BaseOpMode

abstract class NakiriOpMode : BaseOpMode() {
    lateinit var nakiri: Nakiri

    override fun m_init() {
        BulkDataManager.init(hardwareMap)
        NakiriDashboard.init(telemetry, true)

        Globals.OPMODE_TYPE = if((internalOpModeServices as OpModeManagerImpl).activeOpModeName == "NakiriAuto") {
            OpModeType.AUTO
        } else {
            OpModeType.TELEOP
        }

        nakiri = Nakiri()
        nakiri.reset()
    }

    override fun m_init_loop() {
        BulkDataManager.read()
        nakiri.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

    override fun m_start() {
        // would disable camera here : TODO
    }

    override fun m_loop() {
        BulkDataManager.read()
        nakiri.update()
        nakiri.sendDashboardPacket(false)
        NakiriDashboard.update()
    }

    override fun m_stop() {
        nakiri.reset()
    }

    override val is_comp: Boolean
        get() = Globals.IS_COMP
}
