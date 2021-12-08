package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.lib.math.*
import robotuprising.lib.system.statemachine.StateMachineBuilder
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class NakiriTeleOp : NakiriOpMode() {

    private enum class OuttakeLongStates {
        LIFTING,
        EXTENDING_AND_WAIT,
        DEPOSIT,
        RETRACT_LINKAGE_AND_OUTTAKE,
        LIFT_DOWN
    }

    private val outtakeLongSequence = StateMachineBuilder<OuttakeLongStates>()
        .state(OuttakeLongStates.LIFTING)
        .onEnter { superstructure.requestLiftHigh() }
        .transitionTimed(0.4)
        .state(OuttakeLongStates.EXTENDING_AND_WAIT)
        .onEnter { superstructure.requestLinkageOut() }
        .transition { gamepad2.right_bumper }
        .state(OuttakeLongStates.DEPOSIT)
        .onEnter { superstructure.requestOuttakeOut() }
        .transitionTimed(0.5)
        .state(OuttakeLongStates.RETRACT_LINKAGE_AND_OUTTAKE)
        .onEnter { superstructure.requestOuttakeIn(); superstructure.requestLinkageRetract() }
        .transitionTimed(0.5)
        .state(OuttakeLongStates.LIFT_DOWN)
        .onEnter { superstructure.requestLiftBottom() }
        .transition { true }
        .build()

    private fun driveControl() {
        val scale = if(gamepad1.right_bumper) {
            1.0
        } else {
            0.9
        }

        superstructure.requestAyamePowers(
                MathUtil.cubicScaling(0.6, gamepad1.left_stick_x.d * scale),
                MathUtil.cubicScaling(0.6, -gamepad1.left_stick_y.d * scale),
                MathUtil.cubicScaling(0.85, gamepad1.right_stick_x.d * scale)
        )
    }

    private fun outtakeControl() {
        superstructure.runCloseOuttakeSequence(gamepad2.right_trigger_pressed)
        superstructure.runSharedOuttakeSequence(gamepad2.left_trigger_pressed)

        if (!outtakeLongSequence.running && gamepad2.right_bumper) {
            outtakeLongSequence.reset()
            outtakeLongSequence.start()
        }

        if (outtakeLongSequence.running) {
            outtakeLongSequence.update()
        }
    }

    private fun intakeControl() {
        superstructure.runIntakeSequence(gamepad1.right_trigger_pressed)
    }

    private var debugging = false
    private fun debugControl() {
        if(gamepad2.right_stick_button){
            debugging = !debugging
        }

        if(debugging) {
            when {
                gamepad2.left_bumper -> superstructure.requestIntakeOn()
                gamepad2.right_bumper -> superstructure.requestIntakeOff()
                gamepad1.dpad_right -> superstructure.requestIntakeReverse()
                gamepad2.left_trigger_pressed -> superstructure.requestIntakeRotateIn()
                gamepad2.right_trigger_pressed -> superstructure.requestIntakeRotateOut()
                gamepad2.a -> superstructure.requestLinkageOut()
                gamepad2.b -> superstructure.requestLinkageRetract()
                gamepad2.y -> superstructure.requestLiftHigh()
                gamepad2.x -> superstructure.requestLiftBottom()
                gamepad2.dpad_up -> superstructure.requestOuttakeIn()
                gamepad2.dpad_down -> superstructure.requestOuttakeOut()
                gamepad2.dpad_left -> superstructure.requestOuttakeMedium()
            }
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
