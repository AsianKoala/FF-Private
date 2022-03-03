package asiankoala.ftc21.v2.subsystems.nakiri

import asiankoala.ftc21.v2.util.Globals
import asiankoala.ftc21.v2.hardware.nakiri.NakiriServo
import asiankoala.ftc21.v2.lib.opmode.NakiriDashboard
import asiankoala.ftc21.v2.lib.system.Subsystem

class Linkage : Subsystem {
    private val linkageServo = NakiriServo("linkage")

    private var linkageState = LinkageStates.IN
    private enum class LinkageStates(val pos: Double) {
        IN(Globals.LINKAGE_RETRACT),
        MED(Globals.LINKAGE_MED),
        OUT(Globals.LINKAGE_EXTEND),
        TRANSFER(Globals.LINKAGE_TRANSFER),
        MIDDLE(Globals.LINKAGE_EXTEND_MIDDLE)

    }

    fun retract() {
        linkageState = LinkageStates.IN
    }

    fun extendMed() {
        linkageState = LinkageStates.MED
    }

    fun extend() {
        linkageState = LinkageStates.OUT
    }

    fun extendTransfer() {
        linkageState = LinkageStates.TRANSFER
    }

    fun extendMiddle() {
        linkageState = LinkageStates.MIDDLE
    }

    override fun update() {
        linkageServo.position = linkageState.pos
    }

    override fun sendDashboardPacket(debugging: Boolean) {
//        NakiriDashboard.setHeader("linkage")
//        NakiriDashboard["state"] = linkageState

        if (debugging) {
            NakiriDashboard["state pos"] = linkageState.pos
            NakiriDashboard["linkage transfer pos"] = Globals.LINKAGE_TRANSFER
            NakiriDashboard["linkage med pos"] = Globals.LINKAGE_MED
        }
    }

    override fun reset() {
        retract()
    }
}
