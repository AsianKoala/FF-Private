package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.manager.SubsystemManager
import robotuprising.ftc2021.statemachines.AutoAimBlueStateMachine
import robotuprising.ftc2021.statemachines.IntakeStateMachine
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.ReadyForDepositStateMachine
import robotuprising.ftc2021.subsystems.osiris.OsirisStateMachines
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.util.Constants
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

    override fun mInit() {
        super.mInit()

        Odometry.startPose = Pose(Point(8.0, 60.0), Angle(0.0, AngleUnit.RAD))
    }

    override fun mLoop() {
        super.mLoop()

        dtControl()
        intakeControl()
        depositControl()
    }

    private fun dtControl() {
        Ghost.powers = Pose(
                Point(
                        gamepad1.left_stick_x.d * 0.5,
                        -gamepad1.left_stick_y.d * 0.5
                ),
                Angle(
                        gamepad1.right_stick_x.d * 0.5,
                        AngleUnit.RAW
                )
        )

        if(gamepad1.a) {
            Arm.moveServoToPosition(Constants.armHighPosition)
        }

        else if(gamepad1.b) {
            Arm.moveServoToPosition(Constants.armMediumPosition)
        }
    }

    private fun intakeControl() {
        if(gamepad1.right_trigger_pressed) {
            IntakeStateMachine.start()
        }
    }

    private fun depositControl() {
        if(gamepad1.left_trigger_pressed) {
            ReadyForDepositStateMachine.start()
        }

        if(gamepad1.right_bumper) {
            JustDepositStateMachine.start()
        }
    }
}
