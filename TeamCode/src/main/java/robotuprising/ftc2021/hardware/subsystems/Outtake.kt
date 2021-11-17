package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Outtake : Subsystem() {
    private val leftServo = NakiriServo("outtakeLeft")
    private val rightServo = NakiriServo("outtakeRight")

    private var outtakeState = OuttakeStates.IN
    private enum class OuttakeStates(val leftPos: Double, val rightPos: Double) {
        IN(1.0, 0.0),
        MEDIUM(0.5, 0.5),
        OUT(1.0, 0.5)
    }

    fun rotateIn() {
        outtakeState = OuttakeStates.IN
    }

    fun rotateMedium() {
        outtakeState = OuttakeStates.MEDIUM
    }

    fun rotateOut() {
        outtakeState = OuttakeStates.OUT
    }

    override fun update() {
        leftServo.position = outtakeState.leftPos
        rightServo.position = outtakeState.rightPos
    }

    override fun stop() {

    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "outtake"
        NakiriDashboard["state"] = outtakeState
        NakiriDashboard["left pos"] = outtakeState.leftPos
        NakiriDashboard["right pos"] = outtakeState.rightPos
    }
}
