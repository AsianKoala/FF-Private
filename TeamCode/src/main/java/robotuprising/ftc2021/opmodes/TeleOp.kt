package robotuprising.ftc2021.opmodes

import robotuprising.ftc2021.hardware.Robot
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

class TeleOp : Robot() {

    private var pipeExitOffset = 0
    private var allianceXOffset = 0
    private var allianceYOffset = 0

    /**
     *               main gamepad
     * intake on                        intake reverse
     * n                                    n
     *
     *      n                               n
     * n        n                       n       n
     *      n                                n
     *
     *      dt                              dt
     *
     */

    override fun m_loop() {
        superstructure.requestDriveManagerPowers(
            Pose(Point(
                gamepad1.left_stick_x.d,
                gamepad1.left_stick_y.d),
                Angle(gamepad1.left_stick_x.d, AngleUnit.RAW)
            )
        )

        intakeControl()
        liftControl()
    }

    private fun intakeControl() {
        when {
            gamepad1.left_trigger_pressed -> superstructure.requestIntakeOn()
            gamepad1.right_trigger_pressed -> superstructure.requestIntakeReverse()
            else -> superstructure.requestIntakeOff()
        }
    }

    private fun liftControl() {
        when {
            gamepad1.dpad_up -> superstructure.requestIncrementDefaultLiftLevel()
            gamepad1.dpad_down -> superstructure.requestDecrementDefaultLiftLevel()
            gamepad2.right_trigger_pressed -> superstructure.requestLiftGoToDefault()
            gamepad2.left_bumper -> superstructure.requestLiftReset()
            gamepad2.right_bumper -> superstructure.requestLiftShared()
        }
    }
}
