package robotuprising.ftc2021.opmodes.junk.nakiri.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.openftc.revextensions2.ExpansionHubMotor

class IntakeTest : OpMode() {
    private lateinit var intakeMotor: ExpansionHubMotor

    override fun init() {
        intakeMotor = hardwareMap[ExpansionHubMotor::class.java, "intake"]
    }

    override fun loop() {
        if (gamepad1.a) {
            intakeMotor.power = 0.1
        } else if (gamepad1.b) {
            intakeMotor.power = -0.1
        }
    }
}
