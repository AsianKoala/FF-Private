package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.statemachines.*
import robotuprising.ftc2021.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.BlueAlliance
import robotuprising.lib.opmode.RedAlliance
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@RedAlliance
@TeleOp
open class OsirisRedTeleOp : OsirisOpMode() {
    open val fieldHeadingOffset = -90.0 // we are looking at -90.0 from driver pov

    private val ghost = Ghost
    private val odometry = Odometry

    private var fieldOriented = true

    private fun driveControl() {
        val heading = odometry.currentPosition.h

        val xPower = gamepad1.left_stick_x.d
        val yPower = gamepad1.left_stick_y.d
        val turnPower = gamepad1.right_stick_x.d

        val finalPower = if(fieldOriented) {
            Point(xPower, yPower).rotate(heading.angle + fieldHeadingOffset.radians)
        } else {
            Point(xPower, yPower)
        }

        ghost.manualPowers = Pose(finalPower, Angle(turnPower, AngleUnit.RAW))
    }

    private fun intakeControl() {
        if (gamepad1.right_trigger_pressed) {
            IntakeStateMachine.start()
        }

        IntakeStateMachine.update()
    }

    protected var depositLevel = 3
    private fun depositLevelControl() {
        if(gamepad2.right_bumper && depositLevel != 3) {
            depositLevel++
        } else if(gamepad1.left_bumper && depositLevel != 1) {
            depositLevel--
        }
    }

    open fun depositControl() {
        if(gamepad2.left_trigger_pressed) {
            when(depositLevel) {
                3 -> DepositRedHighStateMachine.start()
                2 -> DepositRedMediumStateMachine.start()
                1 -> DepositRedLowStateMachine.start()
            }
        }

        if(gamepad2.right_trigger_pressed) {
            DepositRedSharedStateMachine.start()
        }

        DepositRedHighStateMachine.update()
        DepositRedMediumStateMachine.update()
        DepositRedLowStateMachine.update()
        DepositRedSharedStateMachine.update()
    }

    open fun duckControl() {
        if(gamepad1.dpad_left) {
            DuckRedStateMachine.start()
        }

        DuckRedStateMachine.update()
    }

    override fun mInit() {
        super.mInit()
        ghost.driveState = Ghost.DriveStates.MANUAL
    }

    override fun mLoop() {
        super.mLoop()
        driveControl()
        intakeControl()
        depositLevelControl()
        depositControl()
        duckControl()
    }
}

@BlueAlliance
@TeleOp
class BlueTeleOp : OsirisRedTeleOp() {
    override val fieldHeadingOffset = 90.0

    override fun depositControl() {
        if(gamepad1.left_trigger_pressed) {
            when(depositLevel) {
                3 -> DepositBlueHighStateMachine.start()
                2 -> DepositBlueMediumStateMachine.start()
                1 -> DepositBlueLowStateMachine.start()
            }
        }

        if(gamepad1.left_bumper) {
            DepositBlueSharedStateMachine.start()
        }

        DepositBlueHighStateMachine.update()
        DepositBlueMediumStateMachine.update()
        DepositBlueLowStateMachine.update()
        DepositBlueSharedStateMachine.update()
    }

    override fun duckControl() {
        if(gamepad1.dpad_left) {
            DuckBlueStateMachine.start()
        }

        DuckBlueStateMachine.update()
    }
}
