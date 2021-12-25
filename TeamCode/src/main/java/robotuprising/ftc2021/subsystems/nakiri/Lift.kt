package robotuprising.ftc2021.subsystems.nakiri

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.hardware.nakiri.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

@Config
class Lift : Subsystem {
    companion object {
        @JvmField var kp = 0.028
        @JvmField var ki = 0.0000
        @JvmField var kd = 0.0004
        @JvmField var kgHigh = 0.2
        @JvmField var kgLow = 0.32
    }
    private val liftLeft = NakiriMotor("liftLeft", false).float.resetEncoder.openLoopControl
    private val liftRight = NakiriMotor("liftRight", false).float.resetEncoder.openLoopControl.reverse

    private var pos = 0.0
    private var target = 0.0
    private var output = 0.0

    private var pidCoeffs = PIDCoefficients(kp, ki, kd)
    private val controller = PIDFController(pidCoeffs, kF = { _, _ ->
        if(liftState == LiftStages.HIGH) {
            kgHigh
        } else {
            kgLow
        }
    })

    private val disabled get() = pos < LiftStages.BOTTOM.position+50 && liftState == LiftStages.BOTTOM

    private var liftState = LiftStages.BOTTOM
    enum class LiftStages(val position: Int) {
        BOTTOM(Globals.LIFT_LOW),
        TRANSFER(Globals.LIFT_LOW),
        MEDIUM(Globals.LIFT_MEDIUM),
        HIGH(Globals.LIFT_HIGH)
    }

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
        controller.targetPosition = liftState.position.toDouble()
    }

    private fun setPower(power: Double) {
        liftLeft.power = power
        liftRight.power = power
    }

    override fun update() {
        pos = liftLeft.position.d
        target = liftState.position.d
        output = controller.update(pos)

        if(disabled) {
            setPower(0.0)
        } else {
            setPower(output)
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard.setHeader("lift")
        NakiriDashboard["state"] = liftState
//        NakiriDashboard["pos"] = pos
//        NakiriDashboard["targe t"] = target
//        NakiriDashboard["controller output"] = output
        NakiriDashboard["disabled"] = disabled
        NakiriDashboard["overheating"] = liftLeft.overTemp

        if (debugging) {
            NakiriDashboard["state position"] = liftState.position
            NakiriDashboard["actual power"] = liftLeft.power
            NakiriDashboard["pid coeffs"] = pidCoeffs
            NakiriDashboard["left velocity"] = liftLeft.velocity
            NakiriDashboard["right velocity"] = liftRight.velocity
            NakiriDashboard["velocity delta"] = liftLeft.velocity - liftRight.velocity
            NakiriDashboard["left temp check"] = liftLeft.overTemp
            NakiriDashboard["right temp check"] = liftRight.overTemp
            NakiriDashboard["left current draw"] = liftLeft.currentDraw
            NakiriDashboard["right current draw"] = liftRight.currentDraw

            liftLeft.sendDataToDashboard()
            liftRight.sendDataToDashboard()
        }
    }

    override fun reset() {
        controller.reset()
        setLevel(LiftStages.BOTTOM)
    }
}