package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

class Lift : Subsystem() {

    private val left = NakiriMotor("leftSlide", false).float.openLoopControl
    private val right = NakiriMotor("rightSlide", false).float.openLoopControl

    private var liftState = LiftStages.RESTING
    enum class LiftStages(val position: Int) {
        RESTING(Globals.LIFT_LOW),
        ALLIANCE_MEDIUM(Globals.LIFT_MED),
        ALLIANCE_HIGH(Globals.LIFT_HIGH)
    }


    private val currPosition get() = left.position
    private var internalPower: Double = 0.d

//    private val pidCoeffs = PIDCoeffs(0.5, 0.0, 0.0)
//    private val controller = PIDFController(pidCoeffs) // todo
    private val pidCoeffs = PIDCoefficients(0.5, 0.0, 0.0)
    private val controller = PIDFController(pidCoeffs, kStatic = 0.0)

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
        controller.targetPosition = liftState.position.toDouble()
    }

    override fun update() {
        internalPower = controller.update(currPosition.d)

        left.power = internalPower
        right.power = internalPower
    }

    override fun stop() {
        controller.reset()
        internalPower = 0.d
        left.power = 0.d
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "lift"
        NakiriDashboard["state"] = liftState
        NakiriDashboard["pid coeffs"] = pidCoeffs
        NakiriDashboard["internal power"] = internalPower
        NakiriDashboard["curr position"] = currPosition
        NakiriDashboard["target position"] = liftState.position
    }
}
