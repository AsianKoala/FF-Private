package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.subsystems.Nakiri
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.*
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class NakiriTeleOp : NakiriOpMode() {
    private fun driveControl() {
        val scale = if(gamepad1.right_bumper) {
            0.7
        } else {
            1.0
        }

        nakiri.requestAyamePowers(
                MathUtil.cubicScaling(0.85, gamepad1.left_stick_x.d * scale),
                MathUtil.cubicScaling(0.85, -gamepad1.left_stick_y.d * scale),
                MathUtil.cubicScaling(0.85, gamepad1.right_stick_x.d * scale)
        )
    }

    private enum class OuttakeLongStates {
        LIFTING,
        EXTENDING_AND_WAIT,
        DEPOSIT,
        RETRACT_LINKAGE_AND_OUTTAKE,
        LIFT_DOWN
    }
    private val outtakeLongSequence = StateMachineBuilder<OuttakeLongStates>()
            .state(OuttakeLongStates.LIFTING)
            .onEnter {
                nakiri.requestLiftHigh()
            }
            .transitionTimed(0.4)
            .state(OuttakeLongStates.EXTENDING_AND_WAIT)
            .onEnter { nakiri.requestLinkageOut() }
            .transition { gamepad1.left_trigger_pressed }
            .state(OuttakeLongStates.DEPOSIT)
            .onEnter { nakiri.requestOuttakeOut() }
            .transitionTimed(0.5)
            .state(OuttakeLongStates.RETRACT_LINKAGE_AND_OUTTAKE)
            .onEnter {
                nakiri.requestOuttakeIn();
                nakiri.requestLinkageRetract()
            }
            .transitionTimed(0.5)
            .state(OuttakeLongStates.LIFT_DOWN)
            .onEnter { nakiri.requestLiftBottom() }
            .transition { true }
            .build()

    private fun outtakeControl() {
        nakiri.runCloseOuttakeSequence(gamepad1.left_bumper)
//        nakiri.runSharedOuttakeSequence(gamepad1.right_bumper)

        if (!outtakeLongSequence.running && gamepad1.left_trigger_pressed) {
            outtakeLongSequence.reset()
            outtakeLongSequence.start()
        }

        if (outtakeLongSequence.running) {
            outtakeLongSequence.update()
        }
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

    override fun m_init() {
        super.m_init()
    }

    override fun m_start() {
        super.m_start()
//        nakiri.startGoingOverPipes() // testing
    }

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        outtakeControl()
//        debugControl()
    }
}
