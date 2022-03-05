package asiankoala.junk.v2.opmodes

import asiankoala.junk.v2.lib.math.Angle
import asiankoala.junk.v2.lib.math.AngleUnit
import asiankoala.junk.v2.lib.math.Point
import asiankoala.junk.v2.lib.math.Pose
import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.lib.util.Extensions.d
import asiankoala.junk.v2.lib.util.GamepadUtil.left_trigger_pressed
import asiankoala.junk.v2.lib.util.GamepadUtil.right_trigger_pressed
import asiankoala.junk.v2.statemachines.AllianceReadyDepositStateMachine
import asiankoala.junk.v2.statemachines.JustDepositStateMachine
import asiankoala.junk.v2.statemachines.SharedReadyDepositStateMachine
import asiankoala.junk.v2.statemachines.blue.DuckBlueStateMachine
import asiankoala.junk.v2.statemachines.blue.IntakeStateMachineBlue
import asiankoala.junk.v2.subsystems.hardware.Ghost
import asiankoala.junk.v2.subsystems.hardware.Intake
import asiankoala.junk.v2.subsystems.hardware.Turret
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime

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
        Ghost.powers = Pose(
            Point(
                gamepad1.left_stick_x.d * 0.7,
                -gamepad1.left_stick_y.d * 0.9
            ),
            Angle(gamepad1.right_stick_x.d * 0.7, AngleUnit.RAW)
        )

        if (gamepad1.right_trigger_pressed) IntakeStateMachineBlue.start()

        if (gamepad1.left_trigger_pressed) {
            if (IntakeStateMachineBlue.shared) SharedReadyDepositStateMachine.start()
            else AllianceReadyDepositStateMachine.start()
        }

        if (gamepad1.dpad_up) DuckBlueStateMachine.start()

        OsirisDashboard["shared"] = IntakeStateMachineBlue.shared
    }

    private fun gp2() {
        if (gamepad2.right_bumper) JustDepositStateMachine.start() // deposit

        if (gamepad2.left_trigger_pressed) Intake.turnReverse() // if jam
    }
}
