package asiankoala.junk.v1.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.openftc.revextensions2.ExpansionHubServo

@TeleOp
@Disabled
class ServoDead : OpMode() {
    private lateinit var intakeLeftPivot: ExpansionHubServo
    private lateinit var intakeRightPivot: ExpansionHubServo

    override fun init() {
        intakeLeftPivot = hardwareMap[ExpansionHubServo::class.java, "intakeLeftPivot"]
        intakeRightPivot = hardwareMap[ExpansionHubServo::class.java, "intakeRightPivot"]
    }

    override fun loop() {
        when {
            gamepad1.a -> intakeLeftPivot.position = 0.0
            gamepad1.b -> intakeLeftPivot.position = 0.5
            gamepad1.x -> intakeLeftPivot.position = 1.0
            gamepad1.dpad_up -> intakeRightPivot.position = 0.0
            gamepad1.dpad_right -> intakeRightPivot.position = 0.5
            gamepad1.dpad_down -> intakeRightPivot.position = 1.0
        }
    }

    /*
    intakeLeftPivot
    intakeRightPivot
    linkage
    outtakeLeft
    outtakeRight
     */
}
