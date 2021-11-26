package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.NakiriOpMode
import robotuprising.lib.math.*
import robotuprising.lib.system.statemachine.StateMachineBuilder
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class NakiriTeleOp : NakiriOpMode() {

    private enum class OuttakeLongStates {
        LIFTING,
        EXTENDING,
        WAITING_FOR_INPUT,
        DEPOSIT,
        RETRACT_LINKAGE_AND_OUTTAKE,
        LIFT_DOWN
    }

    private val outtakeLongSequence = StateMachineBuilder<OuttakeLongStates>()
        .state(OuttakeLongStates.LIFTING)
        .onEnter { superstructure.requestLiftHigh() }
        .transitionTimed(0.5)
        .state(OuttakeLongStates.EXTENDING)
        .onEnter { superstructure.requestLinkageOut() }
        .transitionTimed(0.5)
        .state(OuttakeLongStates.WAITING_FOR_INPUT)
        .transition { gamepad1.right_bumper }
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
        superstructure.requestAyamePowers(
            Pose(
                Point(
                    MathUtil.cubicScaling(0.6, gamepad1.left_stick_x.d),
                    MathUtil.cubicScaling(0.6, -gamepad1.left_stick_y.d)
                ),
                Angle(
                    MathUtil.cubicScaling(0.8, gamepad1.right_stick_x.d),
                    AngleUnit.RAW
                )
            )
        )
    }

    private fun outtakeControl() {
        superstructure.runCloseOuttakeSequence(gamepad1.left_trigger_pressed)
        superstructure.runSharedOuttakeSequence(gamepad1.left_bumper)

        if (!outtakeLongSequence.running && gamepad1.right_bumper) {
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

    private fun debugControl() {
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

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        outtakeControl()
        debugControl()
    }
}
