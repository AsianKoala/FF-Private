package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.statemachines.blue.IntakeStateMachineBlue
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.AllianceReadyDepositStateMachine
import robotuprising.ftc2021.statemachines.blue.DuckBlueStateMachine
import robotuprising.ftc2021.statemachines.SharedReadyDepositStateMachine
import robotuprising.ftc2021.statemachines.red.DuckRedStateMachine
import robotuprising.ftc2021.statemachines.red.IntakeStateMachineRed
import robotuprising.ftc2021.subsystems.osiris.IntakeStopper
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
        Ghost.powers = Pose(Point(
                gamepad1.left_stick_x.d * 0.7,
                -gamepad1.left_stick_y.d),
                Angle(gamepad1.right_stick_x.d * 0.5, AngleUnit.RAW)
        )

        if(gamepad1.right_trigger_pressed) IntakeStateMachineBlue.start()
        if(gamepad1.left_bumper) IntakeStateMachineBlue.shouldCock = true


        if(gamepad1.left_trigger_pressed) {
//            if(IntakeStateMachineBlue.shared) SharedReadyDepositStateMachine.start()
            if(true) SharedReadyDepositStateMachine.start()
            else AllianceReadyDepositStateMachine.start()
        }

        OsirisDashboard["shared"] = IntakeStateMachineBlue.shared
    }

    private fun gp2() {
        if(gamepad2.right_bumper) JustDepositStateMachine.start() // deposit

        if(gamepad2.left_trigger_pressed) Intake.turnReverse() // if jam

        if(gamepad2.right_trigger_pressed) Turret.setTurretLockAngle(180.0) // reset angle

        if(gamepad2.dpad_up) DuckBlueStateMachine.start()

        if(gamepad2.a) Intake.turnOff()

        if(gamepad2.left_bumper && sharedTimer.seconds() > 1.0) { // switch to shared
            IntakeStateMachineBlue.shared = !IntakeStateMachineBlue.shared
            sharedTimer.reset()
        }
    }
}
