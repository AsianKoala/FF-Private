package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.hardware.NakiriOpMode
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.GamepadUtil.left_trigger_pressed
import robotuprising.lib.util.GamepadUtil.right_trigger_pressed

@TeleOp
class NakiriTeleOp : NakiriOpMode() {

    override fun m_loop() {
        super.m_loop()
        driveControl()
        intakeControl()
        winstonControl()
    }

    private fun driveControl() {
        val turn = if(gamepad1.right_bumper) {
            1.0
        } else {
            0.75
        }

        superstructure.requestDriveManagerPowers(
                Pose(
                        Point(
                                gamepad1.left_stick_x.d,
                                -gamepad1.left_stick_y.d
                        ),
                        Angle(gamepad1.right_stick_x.d * turn,
                                AngleUnit.RAW)
                )
        )
    }

    private fun intakeControl() {
        // intake
        when {
            gamepad1.left_bumper -> {
                superstructure.requestIntakeReverse()
            }
            gamepad1.right_bumper -> superstructure.requestIntakeOn()
            else -> superstructure.requestIntakeOff()
        }
        // intake pivot
        when {
            gamepad1.a -> superstructure.requestIntakeRotateOut()
            gamepad1.y -> superstructure.requestIntakeRotateIn()
        }
    }

    private fun winstonControl() {
        // linkage
        when {
            gamepad2.x -> superstructure.requestLinkageRetract()
            gamepad2.y -> superstructure.requestLinkageMedium()
            gamepad2.b -> superstructure.requestLinkageOut()
        }

        // outtake
        when {
            gamepad2.left_trigger_pressed -> superstructure.requestOuttakeOut()
            gamepad2.right_trigger_pressed -> superstructure.requestOuttakeIn()
            gamepad2.dpad_up -> superstructure.requestOuttakeMedium()
        }

        //lift
        when {
            gamepad2.left_bumper -> superstructure.requestLiftHigh()
            gamepad2.right_bumper -> superstructure.requestLiftLow()
        }

        when {
            gamepad2.dpad_left -> superstructure.requestSpinnerOff()
            gamepad2.dpad_right -> superstructure.requestSpinnerOn()
        }
    }
}
