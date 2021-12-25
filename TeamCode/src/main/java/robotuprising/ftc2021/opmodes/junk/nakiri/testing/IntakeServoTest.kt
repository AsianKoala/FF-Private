package robotuprising.ftc2021.opmodes.junk.nakiri.testing

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.openftc.revextensions2.ExpansionHubServo

@TeleOp
@Disabled
class IntakeServoTest : OpMode() {
    lateinit var leftIntake: ExpansionHubServo
    lateinit var rightIntake: ExpansionHubServo

    override fun init() {
        leftIntake = hardwareMap[ExpansionHubServo::class.java, "intakeLeftPivot"]
        rightIntake = hardwareMap[ExpansionHubServo::class.java, "intakeRightPivot"]
    }

    override fun loop() {
        if (gamepad1.a) {
            leftIntake.position = 1.0
            rightIntake.position = 0.0
        }

        if (gamepad1.b) {
            rightIntake.position = 0.0
            leftIntake.position = 1.0
        }
    }
}
