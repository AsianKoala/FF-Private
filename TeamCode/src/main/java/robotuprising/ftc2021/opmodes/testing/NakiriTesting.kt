package robotuprising.ftc2021.opmodes.testing

import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.util.epsilonEquals
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.util.Extensions.d

@TeleOp
@Disabled
class NakiriTesting : OpMode() {

    private lateinit var fl: NakiriMotor
    private lateinit var fr: NakiriMotor
    private lateinit var bl: NakiriMotor
    private lateinit var br: NakiriMotor

    private lateinit var liftLeft: NakiriMotor
    private lateinit var liftRight: NakiriMotor

    private lateinit var intake: NakiriMotor
    private lateinit var intakeLeftPivot: NakiriServo
    private lateinit var intakeRightPivot: NakiriServo

    private lateinit var linkage: NakiriServo
    private lateinit var outtakeLeft: NakiriServo
    private lateinit var outtakeRight: NakiriServo

    private val controller = PIDFController(PIDCoefficients(0.05, 0.0, 0.0))
    private var controllerOutput = 0.0

    override fun init() {
        BulkDataManager.init(hardwareMap)
//        fl = NakiriMotor("FL", true).brake.openLoopControl
//        fr = NakiriMotor("FR", true).brake.openLoopControl
//        bl = NakiriMotor("BL", true).brake.openLoopControl
//        br = NakiriMotor("BR", true).brake.openLoopControl
//
//        liftLeft = NakiriMotor("liftLeft", false).float
//        liftRight = NakiriMotor("liftRight", false).float
//        liftLeft.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftRight.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
//        liftLeft.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftRight.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//        liftLeft.direction = DcMotorSimple.Direction.REVERSE
//
//        intake = NakiriMotor("intake", false).brake.openLoopControl
//
//        intakeLeftPivot = NakiriServo("intakeLeftPivot")
//        intakeRightPivot = NakiriServo("intakeRightPivot")
//
//        linkage = NakiriServo("linkage")
//        outtakeLeft = NakiriServo("outtakeLeft")
//        outtakeRight = NakiriServo("outtakeRight")
    }

    override fun init_loop() {
//        telemetry.addLine(liftLeft.zeroPowerBehavior.toString())
    }

    override fun loop() {
        BulkDataManager.read()
        driveControl()
        outtakeControl()
        intakeControl()
        liftControl()
    }

    private fun driveControl() {
        val y = -gamepad1.left_stick_y.d
        val x = gamepad1.left_stick_x.d
        var turn = gamepad1.right_stick_x.d * 0.75

        if (gamepad1.right_bumper) {
            turn = gamepad1.right_stick_x.d
        }

        fl.power = -y - x - turn
        bl.power = -y + x - turn
        fr.power = y - x - turn
        br.power = y + x - turn
    }

    private fun intakeControl() {
        if (gamepad1.a) {
            intakeLeftPivot.position = Globals.INTAKE_PIVOT_LEFT_OUT
            intakeRightPivot.position = Globals.INTAKE_PIVOT_RIGHT_OUT
        }

        if (gamepad1.y) {
            intakeLeftPivot.position = Globals.INTAKE_PIVOT_LEFT_IN
            intakeRightPivot.position = Globals.INTAKE_PIVOT_RIGHT_IN
        }

        intake.power = when {
            gamepad1.left_bumper -> Globals.INTAKE_TRANSFER_POWER
            gamepad1.right_bumper -> Globals.INTAKE_IN_POWER
            else -> Globals.INTAKE_NO_POWER
        }
    }

    private fun outtakeControl() {
        if (gamepad1.x)
            linkage.position = Globals.LINKAGE_RETRACT
        if (gamepad1.b)
            linkage.position = Globals.LINKAGE_EXTEND

        if (gamepad1.dpad_left) {
            outtakeLeft.position = Globals.OUTTAKE_LEFT_IN
            outtakeRight.position = Globals.OUTTAKE_RIGHT_IN
        }

        if (gamepad1.dpad_up) {
            outtakeLeft.position = Globals.OUTTAKE_LEFT_MED
            outtakeRight.position = Globals.OUTTAKE_RIGHT_MED
        }

        if (gamepad1.dpad_right) {
            outtakeLeft.position = Globals.OUTTAKE_LEFT_OUT
            outtakeRight.position = Globals.OUTTAKE_RIGHT_OUT
        }
    }

    private fun liftControl() {
        if (gamepad1.left_trigger > 0.5) {
            controller.reset()
            controller.targetPosition = 60.0
        }

        if (gamepad1.right_trigger > 0.5) {
            controller.reset()
            controller.targetPosition = 400.0
        }

        controllerOutput = controller.update(-liftLeft.position.d)

        if (!(controllerOutput epsilonEquals 0.0)) {
            if (controller.targetPosition == 50.0) {
                liftLeft.power = Range.clip(controllerOutput, 0.1, 0.75)
                liftRight.power = Range.clip(controllerOutput, 0.1, 0.75)
            } else {
                liftLeft.power = Range.clip(controllerOutput, -0.25, 0.8)
                liftRight.power = Range.clip(controllerOutput, -0.25, 0.8)
            }
        }

        telemetry.addData("liftLeft pos", liftLeft.position)
        telemetry.addData("controller output", controllerOutput)
    }
}
// 330
// 530
