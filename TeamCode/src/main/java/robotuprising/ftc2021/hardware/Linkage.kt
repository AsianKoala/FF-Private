package robotuprising.ftc2021.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.system.Subsystem

class Linkage : Subsystem() {
    private val linkageServo = NakiriServo("linkage")

    private var linkageState = LinkageStates.IN
    private enum class LinkageStates {
        IN,
        SHARED_HUB,
        ALLIANCE_HUB
    }

    override fun init(hwMap: HardwareMap) {

    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun sendDashboardPacket() {
        TODO("Not yet implemented")
    }
}