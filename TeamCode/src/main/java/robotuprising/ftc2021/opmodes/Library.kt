package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.util.Extensions.d

@TeleOp(name = "alan click this")
class Library : OpMode() {

    lateinit var fl: ExpansionHubMotor
    lateinit var fr: ExpansionHubMotor
    lateinit var bl: ExpansionHubMotor
    lateinit var br: ExpansionHubMotor

    override fun init() {
        fl = hardwareMap[ExpansionHubMotor::class.java, "FL"]
        fr = hardwareMap[ExpansionHubMotor::class.java,  "FR"]
        bl = hardwareMap[ExpansionHubMotor::class.java, "BL"]
        br = hardwareMap[ExpansionHubMotor::class.java, "BR"]

        fl.direction = DcMotorSimple.Direction.REVERSE
        bl.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {
        fl.power = -gamepad1.left_stick_y.d
        bl.power = -gamepad1.left_stick_y.d
        fr.power = -gamepad1.right_stick_y.d
        br.power = -gamepad1.right_stick_y.d
    }
}