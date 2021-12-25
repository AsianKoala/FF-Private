package robotuprising.ftc2021.opmodes.junk.unused

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import robotuprising.lib.util.Extensions.d

@TeleOp
@Disabled
class BetterLibrary : OpMode() {
    private lateinit var fl: ExpansionHubMotor
    private lateinit var bl: ExpansionHubMotor
    private lateinit var br: ExpansionHubMotor
    private lateinit var fr: ExpansionHubMotor

    override fun init() {
        fl = hardwareMap[ExpansionHubMotor::class.java, "FL"]
        bl = hardwareMap[ExpansionHubMotor::class.java, "BL"]
        br = hardwareMap[ExpansionHubMotor::class.java, "BR"]
        fr = hardwareMap[ExpansionHubMotor::class.java, "FR"]

        fl.direction = DcMotorSimple.Direction.REVERSE
        bl.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {
        fl.power = gamepad1.left_stick_y.d + gamepad1.right_stick_x
        bl.power = gamepad1.left_stick_y.d + gamepad1.right_stick_x
        fr.power = gamepad1.left_stick_y.d - gamepad1.right_stick_x
        br.power = gamepad1.left_stick_y.d - gamepad1.right_stick_x
    }
}
