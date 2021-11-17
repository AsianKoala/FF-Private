package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue

class LiftTest : OpMode() {

    private lateinit var liftLeft: ExpansionHubMotor
    private lateinit var liftRight: ExpansionHubMotor

    override fun init() {
        liftLeft = hardwareMap[ExpansionHubMotor::class.java, "liftLeft"]
        liftRight = hardwareMap[ExpansionHubMotor::class.java, "liftRight"]

        liftLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRight.direction = DcMotorSimple.Direction.REVERSE

        liftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        liftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        setLiftTarget(0)
        liftLeft.mode = DcMotor.RunMode.RUN_TO_POSITION
        liftRight.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    override fun loop() {
        when {
            gamepad1.y -> setLiftTarget(LIFT_HIGH_POS)
            gamepad1.x -> setLiftTarget(LIFT_MIDDLE_POS)
            gamepad1.a -> setLiftTarget(LIFT_LOW_POS)
        }

        if (gamepad1.right_trigger > 0.5 && (liftLeft.targetPosition - liftLeft.currentPosition).absoluteValue < 10) {
            updateLift(0.8)
        } else {
            updateLift(0.0)
        }

        telemetry.addData("lift left enc", liftLeft.currentPosition)
        telemetry.addData("lift right enc", liftRight.currentPosition)
        telemetry.addData("lift mode", liftLeft.mode)
        telemetry.addData("lift power", liftLeft.power)
        telemetry.addData("lift middle pos", LIFT_MIDDLE_POS)
        telemetry.addData("lift high pos", LIFT_HIGH_POS)
    }

    private fun setLiftTarget(target: Int) {
        liftLeft.targetPosition = target
        liftRight.targetPosition = target
    }

    private fun updateLift(power: Double) {
        liftLeft.power = power
        liftRight.power = power
    }

    companion object {
        var LIFT_LOW_POS = 10
        var LIFT_MIDDLE_POS = 200
        var LIFT_HIGH_POS = 500
    }
}
