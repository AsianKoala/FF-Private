package robotuprising.ftc2021.opmodes

import robotuprising.ftc2021.hardware.Robot
import robotuprising.ftc2021.hardware.subsystems.Lift
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed
import robotuprising.lib.util.PrimitiveExtensions.d

class TeleOp : Robot() {

    private var pipeExitOffset = 0
    private var allianceXOffset = 0
    private var allianceYOffset = 0

    private var defaultLiftLevel = 0
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
        superstructure.requestHomuraPowers(
            MecanumPowers(
                gamepad1.left_stick_x.d,
                gamepad1.left_stick_y.d,
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
            gamepad2.dpad_up -> defaultLiftLevel++
            gamepad2.dpad_down -> defaultLiftLevel--
            gamepad2.left_trigger_pressed -> superstructure.requestDefaultLiftTarget(Lift.LiftStages.values()[defaultLiftLevel])
            gamepad2.right_trigger_pressed -> superstructure.requestLiftGoToDefault()
            gamepad2.left_bumper -> superstructure.requestLiftReset()
            gamepad2.right_bumper -> superstructure.requestLiftShared()
        }
    }
}
