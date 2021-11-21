package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.NakiriOpMode
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

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        winstonControl()
    }

    private fun driveControl() {
        val scale = if(gamepad1.right_bumper) {
            1.0
        } else {
            0.7
        }
        superstructure.requestDriveManagerPowers(
                Pose(
                        Point(
                                gamepad1.left_stick_x.d * scale,
                                -gamepad1.left_stick_y.d * scale
                        ),
                        Angle(gamepad1.right_stick_x.d * scale * 0.9,
                                AngleUnit.RAW)
                )
        )
    }

    private fun intakeControl() {
        // intake
        when {
            gamepad1.left_bumper -> {
                superstructure.requestIntakeReverse()
            }
            gamepad1.right_bumper -> superstructure.requestIntakeOn()
            else -> superstructure.requestIntakeOff()
        }
        // intake pivot
        when {
            gamepad1.a -> superstructure.requestIntakeRotateOut()
            gamepad1.y -> superstructure.requestIntakeRotateIn()
        }
    }


    private enum class IntakeSequenceStates {
        INTAKING,
        ROTATING,
        TRANSFERRING,
        OUTTAKE_TO_MEDIUM
    }
    private val intakeSequence = StateMachineBuilder<IntakeSequenceStates>()
            .state(IntakeSequenceStates.INTAKING)
            .build()

    private enum class OuttakeSequenceStates {
        LIFT_UP,
        LINKAGE_OUT,
        DEPOSIT,
        RESET
    }

    private val outtakeSequence = StateMachineBuilder<OuttakeSequenceStates>()
            .state(OuttakeSequenceStates.LIFT_UP)
            .onEnter { superstructure.requestLiftHigh() }
            .transitionTimed(1.25)
            .state(OuttakeSequenceStates.LINKAGE_OUT)
            .onEnter { superstructure.requestLinkageMedium() }
            .transitionTimed(0.5)
            .state(OuttakeSequenceStates.DEPOSIT)
            .onEnter { superstructure.requestOuttakeOut() }
            .transitionTimed(0.75)
            .state(OuttakeSequenceStates.RESET)
            .onEnter {
                superstructure.requestOuttakeMedium()
                superstructure.requestLinkageRetract()
                superstructure.requestLiftLow()
            }
            .build()

    private fun winstonControl() {

        when {
            gamepad2.dpad_down -> {
                outtakeSequence.reset()
                outtakeSequence.start()
            }
        }

        if(outtakeSequence.running) {
            outtakeSequence.update()
        }
    }
}
