package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.system.Subsystem

class Outtake : Subsystem() {
    companion object {
        private const val LEFT_OUT = 1.0
        private const val LEFT_IN = 0.0
        private const val RIGHT_OUT = 1.0
        private const val RIGHT_IN = 0.0
    }

    private val leftServo = NakiriServo("outtakeLeft")
    private val rightServo = NakiriServo("outtakeRight")

    private var outtakeState = OuttakeStates.IN
    private enum class OuttakeStates {
        IN,
        OUT
    }

    fun rotateOut() {
        outtakeState = OuttakeStates.OUT
    }

    fun rotateIn() {
        outtakeState = OuttakeStates.IN
    }

//    override fun init(hwMap: HardwareMap) {
//
//    }

    override fun update() {
        leftServo.position = when (outtakeState) {
            OuttakeStates.IN -> LEFT_IN
            OuttakeStates.OUT -> LEFT_OUT
        }

        rightServo.position = when (outtakeState) {
            OuttakeStates.IN -> RIGHT_IN
            OuttakeStates.OUT -> RIGHT_OUT
        }
    }

    override fun stop() {

    }

    override fun sendDashboardPacket() {
    }
}
