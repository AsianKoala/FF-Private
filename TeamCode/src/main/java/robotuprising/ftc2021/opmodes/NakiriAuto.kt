package robotuprising.ftc2021.opmodes

import robotuprising.ftc2021.util.Globals
import robotuprising.lib.opmode.AllianceSide

abstract class NakiriAuto : NakiriOpMode() {
    abstract val alliance: AllianceSide

    val 
    override fun m_init() {
        super.m_init()
        Globals.ALLIANCE_SIDE = alliance
    }

    override fun m_init_loop() {
        super.m_init_loop()
    }

    override fun m_loop() {
        super.m_loop()
    }
}