package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.manager.SubsystemManager
import robotuprising.ftc2021.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.subsystems.osiris.hardware.Intake
import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class OsirisTeleOpScuffed : OsirisOpMode() {
    private val subsystemManager = SubsystemManager

    private val ghost = Ghost
    private val intake = Intake
    private val odometry = Odometry

    override fun register() {
        subsystemManager.registerSubsystems(
                ghost,
                odometry,
                intake
        )
    }

    override fun mStart() {
        super.mStart()

        
    }

    override fun mLoop() {
        super.mLoop()

        dtControl()
//        intakeControl()
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
        if(gamepad1.right_trigger_pressed) {
            intake.turnOn()
        } else if(gamepad1.left_trigger_pressed) {
            intake.turnReverse()
        } else {
            intake.turnOff()
        }
    }
}