package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.control.motion.PIDCoeffs
import robotuprising.lib.control.motion.PIDFController
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

class Lift : Subsystem() {

    private val left = NakiriMotor("leftSlide", false).brake.openLoopControl
    private val right = NakiriMotor("rightSlide", false).brake.openLoopControl

    private var liftState = LiftStages.RESTING
    enum class LiftStages(val position: Int) {
        RESTING(0),
        ALLIANCE_MEDIUM(250),
        ALLIANCE_HIGH(500)
    }


    private val currPosition get() = left.position
    private var internalPower: Double = 0.d

    private val pidCoeffs = PIDCoeffs(0.5, 0.0, 0.0)
    private val controller = PIDFController(pidCoeffs) // todo

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
        NakiriDashboard["lift state"] = liftState
        NakiriDashboard["lift pid coeffs"] = pidCoeffs
        NakiriDashboard["lift internal power"] = internalPower
        NakiriDashboard["curr position"] = currPosition
        NakiriDashboard["target position"] = liftState.position
    }
}
