package neil.ftc21.v2.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import neil.ftc21.v2.statemachines.blue.IntakeStateMachineBlue
import neil.ftc21.v2.statemachines.JustDepositStateMachine
import neil.ftc21.v2.statemachines.AllianceReadyDepositStateMachine
import neil.ftc21.v2.statemachines.blue.DuckBlueStateMachine
import neil.ftc21.v2.statemachines.SharedReadyDepositStateMachine
import neil.ftc21.v2.subsystems.osiris.hardware.*
import neil.lib.math.Angle
import neil.lib.math.AngleUnit
import neil.lib.math.Point
import neil.lib.math.Pose
import neil.lib.opmode.OsirisDashboard
import neil.lib.util.Extensions.d
import neil.lib.util.GamepadUtil.left_trigger_pressed
import neil.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class OsirisBlueTeleOp : OsirisOpMode() {
    override fun mInit() {
        super.mInit()
        Turret.zero()
    }

    override fun mStart() {
        Turret.setTurretLockAngle(180.0)
        super.mStart()
        Ghost.driveState = Ghost.DriveStates.MANUAL
    }

    override fun mLoop() {
        super.mLoop()
        gp1()
        gp2()
    }

    private var sharedTimer = ElapsedTime()
    private fun gp1() {
        Ghost.powers = Pose(Point(
                gamepad1.left_stick_x.d * 0.7,
                -gamepad1.left_stick_y.d * 0.9),
                Angle(gamepad1.right_stick_x.d * 0.7, AngleUnit.RAW)
        )

        if(gamepad1.right_trigger_pressed) IntakeStateMachineBlue.start()

        if(gamepad1.left_trigger_pressed) {
            if(IntakeStateMachineBlue.shared) SharedReadyDepositStateMachine.start()
            else AllianceReadyDepositStateMachine.start()
        }

        if(gamepad1.dpad_up) DuckBlueStateMachine.start()

        OsirisDashboard["shared"] = IntakeStateMachineBlue.shared
    }

    private fun gp2() {
        if(gamepad2.right_bumper) JustDepositStateMachine.start() // deposit

        if(gamepad2.left_trigger_pressed) Intake.turnReverse() // if jam

    }
}
