package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.lib.util.Extensions.d

@TeleOp
class NakiriTesting : OpMode() {
    private lateinit var fl: ExpansionHubMotor
    private lateinit var fr: ExpansionHubMotor
    private lateinit var bl: ExpansionHubMotor
    private lateinit var br: ExpansionHubMotor

    private lateinit var liftLeft: ExpansionHubMotor
    private lateinit var liftRight: ExpansionHubMotor

    private lateinit var linkage: ExpansionHubServo

    override fun init() {
        fl = hardwareMap[ExpansionHubMotor::class.java, "FL"]
        fr = hardwareMap[ExpansionHubMotor::class.java, "FR"]
        bl = hardwareMap[ExpansionHubMotor::class.java, "BL"]
        br = hardwareMap[ExpansionHubMotor::class.java, "BR"]

        liftLeft = hardwareMap[ExpansionHubMotor::class.java, "liftLeft"]
        liftRight = hardwareMap[ExpansionHubMotor::class.java, "liftRight"]
        liftRight.direction = DcMotorSimple.Direction.REVERSE
        liftLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        liftRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        linkage = hardwareMap[ExpansionHubServo::class.java,"linkage"]
    }

    override fun loop() {

        val y = -gamepad1.left_stick_y.d
        val x = gamepad1.left_stick_x.d
        val turn = gamepad1.right_stick_x.d

        fl.power = y + x - turn
        bl.power = y - x - turn
        fr.power = -y - x - turn
        br.power = -y + x - turn

        val liftPower = 0.75
        when {
            gamepad1.left_trigger > 0.5 -> {
                liftLeft.power = liftPower
                liftRight.power = liftPower
            }

            gamepad1.right_trigger > 0.5 -> {
                liftLeft.power = -liftPower
                liftRight.power = -liftPower

            }

            else -> {
                liftLeft.power = 0.0
                liftRight.power = 0.0
            }
        }

        val linkageIn = 1.0
        val linkageOut = 0.5

        if(gamepad1.x)
            linkage.position = linkageIn
        if(gamepad1.b)
            linkage.position = linkageOut
    }
}