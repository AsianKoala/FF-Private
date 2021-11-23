package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.NakiriOpMode
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
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
        .onEnter {
            superstructure.requestOuttakeIn()
            superstructure.requestLinkageRetract()
        }
        .transitionTimed(0.5)
        .state(OuttakeLongStates.LIFT_DOWN)
        .onEnter { superstructure.requestLiftLow() }
        .transition { true }
        .build()

    private fun driveControl() {
        superstructure.requestAyamePowers(
            Pose(
                Point(
                    gamepad1.left_stick_x.d * 0.75,
                    -gamepad1.left_stick_y.d * 0.75
                ),
                Angle(
                    gamepad1.right_stick_x.d * 0.75,
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

        outtakeLongSequence.update()

        if (gamepad1.y) {
            Globals.LINKAGE_CUSTOM += 0.05
        }

        if (gamepad1.a) {
            Globals.LINKAGE_CUSTOM -= 0.05
        }
    }

    private fun intakeControl() {
        superstructure.runIntakeSequence(gamepad1.right_trigger_pressed)
    }

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        outtakeControl()
    }
}
