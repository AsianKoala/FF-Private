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
import asiankoala.junk.v2.statemachines.red.DuckRedStateMachine
import asiankoala.junk.v2.statemachines.red.IntakeStateMachineRed
import asiankoala.junk.v2.subsystems.hardware.Ghost
import asiankoala.junk.v2.subsystems.hardware.Intake
import asiankoala.junk.v2.subsystems.hardware.Turret
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp
class OsirisRedTeleOp : OsirisOpMode() {

    override fun mInit() {
        super.mInit()
        Turret.zero()
    }

    override fun mStart() {
        super.mStart()
        Ghost.driveState = Ghost.DriveStates.MANUAL
        Turret.setTurretLockAngle(180.0)
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
                gamepad1.left_stick_x.d,
                -gamepad1.left_stick_y.d
            ),
            Angle(
                gamepad1.right_stick_x.d, AngleUnit.RAW
            )
        )

        if (gamepad1.right_trigger_pressed) IntakeStateMachineRed.start()
        if (gamepad1.left_bumper) IntakeStateMachineRed.shouldCock = true

        if (gamepad1.left_trigger_pressed) {
            if (IntakeStateMachineRed.shared) SharedReadyDepositStateMachine.start()
            else AllianceReadyDepositStateMachine.start()
        }

        OsirisDashboard["shared"] = IntakeStateMachineRed.shared
    }

    private fun gp2() {
        if (gamepad2.right_bumper) JustDepositStateMachine.start() // deposit

        if (gamepad2.left_trigger_pressed) Intake.turnReverse() // if jam

        if (gamepad2.right_trigger_pressed) Intake.turnOff()

        if (gamepad2.left_bumper && sharedTimer.seconds() > 1.0) { // switch to shared
            IntakeStateMachineRed.shared = !IntakeStateMachineRed.shared
            sharedTimer.reset()
        }

        if (gamepad2.dpad_up) DuckRedStateMachine.start()
    }
}
