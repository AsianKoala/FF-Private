package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Linkage : Subsystem() {
    private val linkageServo = NakiriServo("linkage")

    private var linkageState = LinkageStates.IN
    private enum class LinkageStates(val pos: Double) {
        IN(0.0),
        ALLIANCE_HUB(1.0)
    }

    fun extend() {
        linkageState = LinkageStates.ALLIANCE_HUB
    }

    fun retract() {
        linkageState = LinkageStates.IN
    }

    override fun update() {
        linkageServo.position = linkageState.pos
    }

    override fun stop() {

    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "linkage"
        NakiriDashboard["state"] = linkageState
        NakiriDashboard["state pos"] = linkageState.pos
    }
}
