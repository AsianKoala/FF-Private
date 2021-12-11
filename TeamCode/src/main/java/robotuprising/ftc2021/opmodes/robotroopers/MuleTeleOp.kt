package robotuprising.ftc2021.opmodes.robotroopers

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.util.Extensions.d
import com.acmerobotics.roadrunner.control.PIDFController
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.util.Range
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
@Disabled
class MuleTeleOp : OpMode() {
    private lateinit var fl: ExpansionHubMotor
    private lateinit var fr: ExpansionHubMotor
    private lateinit var bl: ExpansionHubMotor
    private lateinit var br: ExpansionHubMotor

    private lateinit var motors: List<ExpansionHubMotor>

    private lateinit var arm: ExpansionHubMotor
    private lateinit var intake: ExpansionHubMotor

    private var kp = 0.015
    private var ki = 0.003
    private var kd = 0.00065
    private var armDepositPosition = 420.0
    private var armRestPosition = 10.0
    private var min = -0.5
    private var max = 0.5

    private var controller = PIDFController(PIDCoefficients(kp, ki, kd))


    override fun init() {
        fr = hardwareMap[ExpansionHubMotor::class.java, "FR"]
        fl = hardwareMap[ExpansionHubMotor::class.java, "FL"]
        br = hardwareMap[ExpansionHubMotor::class.java, "BR"]
        bl = hardwareMap[ExpansionHubMotor::class.java, "BL"]

        motors = listOf(fr, fl, br, bl)
        motors.forEach {
            it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        }

        fl.direction = DcMotorSimple.Direction.REVERSE
        bl.direction = DcMotorSimple.Direction.REVERSE

        arm = hardwareMap[ExpansionHubMotor::class.java, "Arm"]
        arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        arm.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        arm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        intake = hardwareMap[ExpansionHubMotor::class.java, "Intake"]
        intake.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
        val y = -gamepad1.left_stick_y.d
        val x = gamepad1.left_stick_x.d
        val turn = gamepad1.right_stick_x.d

        fl.power = y + x + turn;
        fr.power = y - x - turn;
        bl.power = y - x + turn;
        br.power = y + x - turn;

        val output = controller.update(arm.currentPosition.d)
        arm.power = Range.clip(output, min, max)

        if(gamepad1.right_bumper) {
            controller.targetPosition = armDepositPosition
        }

        if(gamepad1.left_bumper) {
            controller.targetPosition = 10.0
        }

        intake.power = when {
            gamepad1.left_trigger_pressed -> 1.0
            gamepad1.right_trigger_pressed -> -1.0
            else -> 0.0
        }
    }
}
