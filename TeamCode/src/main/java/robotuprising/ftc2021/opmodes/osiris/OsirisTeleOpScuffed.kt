package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.manager.SubsystemManager
import robotuprising.ftc2021.statemachines.IntakeStateMachine
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.subsystems.osiris.OsirisStateMachines
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class OsirisTeleOpScuffed : OsirisOpMode() {

    private val ghost = Ghost

    override fun mLoop() {
        super.mLoop()

//        dtControl()
        intakeControl()
    }

    private fun dtControl() {
        ghost.powers = Pose(
                Point(
                        gamepad1.left_stick_x.d,
                        -gamepad1.left_stick_y.d
                ),
                Angle(
                        gamepad1.right_stick_x.d,
                        AngleUnit.RAW
                )
        )
    }

    private fun intakeControl() {

//        if(gamepad1.right_trigger_pressed) {
//            IntakeStateMachine.start()
//        }
//
//        if(gamepad1.left_trigger_pressed) {
//            JustDepositStateMachine.start()
//        }
//
//        if(gamepad1.left_bumper) {
//            Outtake.depositHigh()
//        }
//
//        if(gamepad1.right_bumper) {
//            Arm.depositHigh()
//        }

        if(gamepad1.a) {
            Turret.disabled = false
        }
    }
}
