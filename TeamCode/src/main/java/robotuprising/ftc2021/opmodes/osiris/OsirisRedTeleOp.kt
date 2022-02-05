package robotuprising.ftc2021.opmodes.osiris

import robotuprising.ftc2021.statemachines.AllianceReadyDepositStateMachine
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.SharedReadyDepositStateMachine
import robotuprising.ftc2021.statemachines.red.DuckRedStateMachine
import robotuprising.ftc2021.statemachines.red.IntakeStateMachineRed
import robotuprising.ftc2021.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.subsystems.osiris.hardware.Intake
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

class OsirisRedTeleOp : OsirisOpMode() {
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

    override fun mStop() {
        super.mStop()
        Constants.END_OF_AUTO_TURRET_VALUE = Double.NaN
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
        if(gamepad1.right_trigger_pressed) IntakeStateMachineRed.start()
        if(gamepad1.left_bumper) {
            Intake.turnReverse()
        }

        if(gamepad1.a) {
            IntakeStateMachineRed.shared = !IntakeStateMachineRed.shared
        }

        OsirisDashboard["shared"] = IntakeStateMachineRed.shared
    }

    private fun depositControl() {
        if(gamepad1.left_trigger_pressed) {
            if(IntakeStateMachineRed.shared) {
                SharedReadyDepositStateMachine.start()
            } else {
                AllianceReadyDepositStateMachine.start()
            }
        }
        if(gamepad1.right_bumper) JustDepositStateMachine.start()
    }

    private fun duckControl() {
        if(gamepad1.dpad_up) DuckRedStateMachine.start()
    }
}