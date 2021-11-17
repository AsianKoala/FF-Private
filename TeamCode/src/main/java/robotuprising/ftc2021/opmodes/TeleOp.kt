package robotuprising.ftc2021.opmodes

import robotuprising.ftc2021.hardware.Robot
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

// TODO
class TeleOp : Robot() {

    private val allianceHub = Point.ORIGIN

    override fun m_loop() {
        driveControl()
        intakeControl()
        outtakeControl()
    }

    private fun driveControl() {
        superstructure.requestDriveManagerPowers(
                Pose(
                        Point(
                                -gamepad1.left_stick_x.d,
                                gamepad1.left_stick_y.d
                        ),
                        Angle(gamepad1.left_stick_x.d, AngleUnit.RAW)
                )
        )
    }
    private fun intakeControl() {
        when {
            gamepad1.left_trigger_pressed -> superstructure.requestIntakeOn()
            gamepad1.right_trigger_pressed -> superstructure.requestIntakeReverse()
            gamepad1.left_bumper -> superstructure.requestIntakeRotateOut()
            gamepad1.right_bumper -> superstructure.requestIntakeRotateOut()
            else -> superstructure.requestIntakeOff()
        }
    }

    private fun outtakeControl() {
        when {
            gamepad1.y -> {
                superstructure.requestLiftHigh()
                superstructure.requestLinkageOut()
                superstructure.requestOuttakeMedium()
            }
            gamepad1.a -> {
                superstructure.requestLiftRest()
                superstructure.requestLinkageRetract()
                superstructure.requestOuttakeIn()
            }

            gamepad1.b -> {
                superstructure.requestOuttakeOut()
            }
        }
    }
}
