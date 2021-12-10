package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.*
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp()
class NakiriTeleOp : NakiriOpMode() {

    private fun driveControl() {
        val scale = if(gamepad1.right_bumper) {
            0.8
        } else {
            1.0
        }

        nakiri.requestAyamePowers(
                MathUtil.cubicScaling(0.6, gamepad1.left_stick_x.d * scale),
                MathUtil.cubicScaling(0.6, -gamepad1.left_stick_y.d * scale),
                MathUtil.cubicScaling(0.85, gamepad1.right_stick_x.d * scale)
        )
    }

    private fun outtakeControl() {
        nakiri.runTeleLongOuttakeSequence(gamepad1.left_trigger_pressed)
        nakiri.runCloseOuttakeSequence(gamepad1.left_bumper)
        nakiri.runSharedOuttakeSequence(gamepad1.right_bumper)
    }

    private fun intakeControl() {
        nakiri.runIntakeSequence(gamepad1.right_trigger_pressed)
    }

    private fun debugControl() {
        when {
            gamepad2.right_trigger_pressed -> nakiri.requestLiftHigh()
            gamepad2.left_trigger_pressed -> nakiri.requestLiftTransfer()
            gamepad2.right_bumper -> nakiri.requestLiftBottom()
            gamepad2.a -> nakiri.requestOuttakeIn()
            gamepad2.b -> nakiri.requestOuttakeOut()
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
