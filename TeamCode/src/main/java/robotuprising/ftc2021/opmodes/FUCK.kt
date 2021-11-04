package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.openftc.revextensions2.ExpansionHubMotor

class FUCK : OpMode() {

    lateinit var motor: ExpansionHubMotor

    override fun init() {
        motor = hardwareMap[ExpansionHubMotor::class.java, "m"]
    }

    override fun loop() {
        motor.power = 1.0
    }
}
