package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotorFactory
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d

// todo change lift pid to inches so i can actually tune without wondering wtf the coeffs are
class Lift : Subsystem {
    private val liftLeft = NakiriMotorFactory
            .name("liftLeft")
            .slave
            .resetEncoder
            .openLoopControl
            .float
            .reverse
            .create

    private val liftRight = NakiriMotorFactory
            .name("liftRight")
            .slave
            .resetEncoder
            .openLoopControl
            .float
            .create

    private var liftState = LiftStages.LOW
    enum class LiftStages(val position: Int) {
        LOW(Globals.LIFT_LOW),
        HIGH(Globals.LIFT_HIGH)
    }

    private val controller = PIDFController(Globals.PID_COEFFS)
    private var controllerOutput = 0.0

    fun setLevel(stage: LiftStages) {
        liftState = stage
        controller.reset()
        controller.targetPosition = liftState.position.toDouble()
    }

    override fun update() {
        controllerOutput = controller.update(-liftLeft.position.d)

        if (!(controllerOutput epsilonEquals 0.0)) {
            if (controller.targetPosition epsilonEquals Globals.LIFT_LOW.d) {
                liftLeft.power = Range.clip(controllerOutput, 0.1, 0.75)
                liftRight.power = Range.clip(controllerOutput, 0.1, 0.75)
            } else {
                liftLeft.power = Range.clip(controllerOutput, -0.25, 0.8)
                liftRight.power = Range.clip(controllerOutput, -0.25, 0.8)
            }
        }
    }

    override fun stop() {
        controller.reset()
        liftLeft.power = 0.d
        liftRight.power = 0.d
    }

    override fun sendDashboardPacket() {
//        NakiriDashboard.name = "lift"
//        NakiriDashboard["state"] = liftState
//        NakiriDashboard["pid coeffs"] = pidCoeffs
//        NakiriDashboard["internal power"] = internalPower
//        NakiriDashboard["curr position"] = currPosition
//        NakiriDashboard["target position"] = liftState.position
    }

//    init {
//        liftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftLeft.direction = DcMotorSimple.Direction.REVERSE
//    }
}
