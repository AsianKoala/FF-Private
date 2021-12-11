package robotuprising.ftc2021.subsystems

import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Outtake : Subsystem {
    private val leftServo = NakiriServo("outtakeLeft")
    private val rightServo = NakiriServo("outtakeRight")

    private var outtakeState = OuttakeStates.MEDIUM
    private enum class OuttakeStates(val leftPos: Double, val rightPos: Double) {
        IN(Globals.OUTTAKE_LEFT_IN, Globals.OUTTAKE_RIGHT_IN),
        MEDIUM(Globals.OUTTAKE_LEFT_MED, Globals.OUTTAKE_RIGHT_MED),
        OUT(Globals.OUTTAKE_LEFT_OUT, Globals.OUTTAKE_RIGHT_OUT)
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

    override fun sendDashboardPacket(debugging: Boolean) {
//        NakiriDashboard.setHeader("outtake")
//        NakiriDashboard["state"] = outtakeState

        if (debugging) {
            NakiriDashboard["left pos"] = outtakeState.leftPos
            NakiriDashboard["right pos"] = outtakeState.rightPos
        }
    }

    override fun reset() {
        rotateMedium()
    }
}
