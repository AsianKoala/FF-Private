package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.statemachines.blue.IntakeStateMachineBlue
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.AllianceReadyDepositStateMachine
import robotuprising.ftc2021.statemachines.blue.DuckBlueStateMachine
import robotuprising.ftc2021.statemachines.SharedReadyDepositStateMachine
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
class OsirisBlueTeleOp : OsirisOpMode() {

    override fun mStart() {
        super.mLoop()
        Ghost.driveState = Ghost.DriveStates.MANUAL
    }

    override fun mLoop() {
        super.mLoop()
        dtControl()
        intakeControl()
        depositControl()
        duckControl()
    }

    private fun dtControl() {
        Ghost.powers = Pose(Point(
                            gamepad1.left_stick_x.d * 0.7,
                             -gamepad1.left_stick_y.d),
                Angle(
                        gamepad1.right_stick_x.d * 0.5, AngleUnit.RAW)
        )
    }

    private fun intakeControl() {
        if(gamepad1.right_trigger_pressed) IntakeStateMachineBlue.start()
        if(gamepad1.left_bumper) {
            Intake.turnReverse()
        }

        if(gamepad1.a) {
            IntakeStateMachineBlue.shared = !IntakeStateMachineBlue.shared
        }

        OsirisDashboard["shared"] = IntakeStateMachineBlue.shared
    }

    private fun depositControl() {
        if(gamepad1.left_trigger_pressed) {
            if(IntakeStateMachineBlue.shared) {
                SharedReadyDepositStateMachine.start()
            } else {
                AllianceReadyDepositStateMachine.start()
            }
        }
        if(gamepad1.right_bumper) JustDepositStateMachine.start()
    }

    private fun duckControl() {
        if(gamepad1.dpad_up) DuckBlueStateMachine.start()
    }
}
