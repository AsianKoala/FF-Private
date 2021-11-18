package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.util.Extensions.d

@TeleOp
class NakiriTesting : OpMode() {
//    private lateinit var fl: ExpansionHubMotor
//    private lateinit var fr: ExpansionHubMotor
//    private lateinit var bl: ExpansionHubMotor
//    private lateinit var br: ExpansionHubMotor
//
//    private lateinit var liftLeft: ExpansionHubMotor
//    private lateinit var liftRight: ExpansionHubMotor
//
//    private lateinit var linkage: ExpansionHubServo
//
//    private lateinit var intake: ExpansionHubMotor
//    private lateinit var intakeLeftPivot: ExpansionHubServo
//    private lateinit var intakeRightPivot: ExpansionHubServo
//
//    private lateinit var outtakeLeft: ExpansionHubServo
//    private lateinit var outtakeRight: ExpansionHubServo

    private val fl = NakiriMotor("FL", true).brake.openLoopControl
    private val fr = NakiriMotor("FR", true).brake.openLoopControl
    private val bl = NakiriMotor("BL", true).brake.openLoopControl
    private val br = NakiriMotor("BR", true).brake.openLoopControl

    private val liftLeft = NakiriMotor("liftLeft", false).float.resetControl.openLoopControl
    private val liftRight = NakiriMotor("liftRight", false).float.resetControl.openLoopControl

    private val intake = NakiriMotor("intake", false).brake.openLoopControl
    private val intakeLeftPivot = NakiriServo("intakeLeftPivot")
    private val intakeRightPivot = NakiriServo("intakeRightPivot")

    private val linkage = NakiriServo("linkage")
    private val outtakeLeft = NakiriServo("outtakeLeft")
    private val outtakeRight = NakiriServo("outtakeRight")

    private val duckSpinner = NakiriMotor("duck", false).brake.openLoopControl

    override fun init() {
//        fl = hardwareMap[ExpansionHubMotor::class.java, "FL"]
//        fr = hardwareMap[ExpansionHubMotor::class.java, "FR"]
//        bl = hardwareMap[ExpansionHubMotor::class.java, "BL"]
//        br = hardwareMap[ExpansionHubMotor::class.java, "BR"]
//
//        fl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        fr.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        bl.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        br.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

//        liftLeft = hardwareMap[ExpansionHubMotor::class.java, "liftLeft"]
//        liftRight = hardwareMap[ExpansionHubMotor::class.java, "liftRight"]
//        liftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftRight.direction = DcMotorSimple.Direction.REVERSE
//        liftLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT
//        liftRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT

//        linkage = hardwareMap[ExpansionHubServo::class.java, "linkage"]
//
//        intake = hardwareMap[ExpansionHubMotor::class.java, "intake"]
//        intakeLeftPivot = hardwareMap[ExpansionHubServo::class.java, "intakeLeftPivot"]
//        intakeRightPivot = hardwareMap[ExpansionHubServo::class.java, "intakeRightPivot"]
//
//        outtakeLeft = hardwareMap[ExpansionHubServo::class.java, "outtakeLeft"]
//        outtakeRight = hardwareMap[ExpansionHubServo::class.java, "outtakeRight"]

//        intake.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    override fun loop() {
        driveControl()
        outtakeControl()
        intakeControl()
        liftControl()
    }

    private fun driveControl() {
        val y = -gamepad1.left_stick_y.d
        val x = gamepad1.left_stick_x.d
        val turn = gamepad1.right_stick_x.d

        fl.power = -y - x - turn
        bl.power = -y + x - turn
        fr.power = y - x - turn
        br.power = y + x - turn
    }

    private fun outtakeControl() {
        if (gamepad1.x)
            linkage.position = 1.0
        if (gamepad1.b)
            linkage.position = 0.5

        if(gamepad1.dpad_left) {
            outtakeLeft.position = 0.35
            outtakeRight.position = 0.25
        }

        if(gamepad1.dpad_right) {
            outtakeLeft.position = 0.0
            outtakeRight.position = 0.60
        }
    }

    private fun intakeControl() {
        if(gamepad1.a) {
            intakeLeftPivot.position = 0.88
            intakeRightPivot.position = 0.02
        }

        if(gamepad1.y) {
            intakeLeftPivot.position = 0.1
            intakeRightPivot.position = 0.75
        }

        if(gamepad1.left_bumper) {
            intake.power = 1.0
        } else if(gamepad1.right_bumper) {
            intake.power = -1.0
        } else {
            intake.power = 0.0
        }
    }

    private fun liftControl() {
        if(gamepad1.right_trigger > 0.5) {
            liftLeft.targetPosition = -380
            liftRight.targetPosition = -380
            liftLeft.power = 0.75
            liftRight.power = 0.75
        }

        if(gamepad1.left_trigger > 0.5) {
            liftLeft.targetPosition = -10
            liftRight.targetPosition = -10
            liftLeft.power = -0.25
            liftRight.power = -0.25
        }

        telemetry.addData("lift left", liftLeft.position)
        telemetry.addData("lift right", liftRight.position)
    }
}
