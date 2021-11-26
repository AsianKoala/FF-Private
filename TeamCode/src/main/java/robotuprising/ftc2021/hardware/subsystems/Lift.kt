package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.qualcomm.robotcore.util.Range
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

// todo tune
@Config
class Lift : Subsystem {
    companion object {
        @JvmField var kp = 0.025
        @JvmField var ki = 0.0015
        @JvmField var kd = 0.001
    }
    private val liftLeft = NakiriMotor("liftLeft", false).float.resetEncoder.openLoopControl.reverse
    private val liftRight = NakiriMotor("liftRight", false).float.resetEncoder.openLoopControl

    private var pos = 0.0
    private var target = 0.0

    private var liftState = LiftStages.BOTTOM
    enum class LiftStages(val position: Int) {
        BOTTOM(Globals.LIFT_LOW),
        TRANSFER(Globals.LIFT_LOW),
        HIGH(Globals.LIFT_HIGH)
    }

    private var pidCoeffs = PIDCoefficients(kp, ki, kd)
    private val controller = PIDFController(pidCoeffs)
    private var controllerOutput = 0.0

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
        controller.targetPosition = liftState.position.toDouble()
    }

    override fun update() {
        controllerOutput = controller.update(-liftLeft.position.d)

        if (!(controllerOutput epsilonEquals 0.0)) {
            if (-liftLeft.position < LiftStages.BOTTOM.position && liftState == LiftStages.BOTTOM) {
                liftLeft.power = 0.0
                liftRight.power = 0.0
            } else if (liftState == LiftStages.TRANSFER || liftState == LiftStages.BOTTOM) {
                liftLeft.power = Range.clip(controllerOutput, 0.05, 0.75)
                liftRight.power = Range.clip(controllerOutput, 0.05, 0.75)
            } else {
                liftLeft.power = Range.clip(controllerOutput, -0.25, 0.8)
                liftRight.power = Range.clip(controllerOutput, -0.25, 0.8)
            }
        }

        pos = -liftLeft.position.d
        target = liftState.position.d
        NakiriDashboard.addData("pos", pos)
        NakiriDashboard.addData("target", target)
    }

    override fun stop() {
        controller.reset()
        liftLeft.power = 0.d
        liftRight.power = 0.d
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard.setHeader("lift")
        NakiriDashboard["state"] = liftState
        NakiriDashboard["state position"] = liftState.position
        NakiriDashboard["controller output"] = controllerOutput
        NakiriDashboard["actual power"] = liftLeft.power
        NakiriDashboard["disabled below zero"] = -liftLeft.position < LiftStages.BOTTOM.position && liftState == LiftStages.BOTTOM
        NakiriDashboard["pid coeffs"] = pidCoeffs
        NakiriDashboard["left velocity"] = liftLeft.velocity
        NakiriDashboard["right velocity"] = liftRight.velocity
        NakiriDashboard["velocity delta"] = liftLeft.velocity - liftRight.velocity
        NakiriDashboard["left temp check"] = liftLeft.overTemp
        NakiriDashboard["right temp check"] = liftRight.overTemp
        NakiriDashboard["left current draw"] = liftLeft.currentDraw
        NakiriDashboard["right current draw"] = liftRight.currentDraw

        if (debugging) {
            liftLeft.sendDataToDashboard()
            liftRight.sendDataToDashboard()
        }
    }
}
