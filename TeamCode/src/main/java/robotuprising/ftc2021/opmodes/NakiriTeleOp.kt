package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.*
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class NakiriTeleOp : NakiriOpMode() {

    private fun driveControl() {
        val scale = if(gamepad1.right_bumper) {
            1.0
        } else {
//            0.9
            1.0//cringe
        }

        nakiri.requestAyamePowers(
                MathUtil.cubicScaling(0.6, gamepad1.left_stick_x.d * scale),
                MathUtil.cubicScaling(0.6, -gamepad1.left_stick_y.d * scale),
                MathUtil.cubicScaling(0.85, gamepad1.right_stick_x.d * scale)
        )
    }

    private fun outtakeControl() {
//        nakiri.runCloseOuttakeSequence(gamepad2.right_trigger_pressed)
//        nakiri.runSharedOuttakeSequence(gamepad2.left_trigger_pressed)
//        nakiri.runTeleLongOuttakeSequence(gamepad2.right_bumper)
    }

    private fun intakeControl() {
        nakiri.runIntakeSequence(gamepad1.right_trigger_pressed)
    }

    private fun debugControl() {
        when {
            gamepad2.right_trigger_pressed -> nakiri.requestLiftHigh()
            gamepad2.left_trigger_pressed -> nakiri.requestLiftTransfer()
        }
    }

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        outtakeControl()
        debugControl()
    }
}
