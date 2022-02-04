package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.statemachines.IntakeStateMachine
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.ReadyForDepositStateMachine
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.util.Constants
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class OsirisTeleOpScuffed : OsirisOpMode() {

    override fun mStart() {
        super.mLoop()
        Ghost.driveState = Ghost.DriveStates.MANUAL
    }

    override fun mLoop() {
        super.mLoop()
        dtControl()
        intakeControl()
        depositControl()
    }

    private fun dtControl() {
        Ghost.powers = Pose(Point(
                        gamepad1.left_stick_x.d * 0.5,
                        -gamepad1.left_stick_y.d * 0.5),
                Angle(
                        gamepad1.right_stick_x.d * 0.5, AngleUnit.RAW)
        )
    }

    private fun intakeControl() {
        if(gamepad1.right_trigger_pressed) IntakeStateMachine.start()
    }

    private fun depositControl() {
        if(gamepad1.left_trigger_pressed) ReadyForDepositStateMachine.start()
        if(gamepad1.right_bumper) JustDepositStateMachine.start()
    }
}
