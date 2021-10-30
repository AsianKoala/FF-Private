package robotuprising.ftc2021.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.CRServo
import org.openftc.revextensions2.ExpansionHubServo

class CRServoTest : OpMode() {

    lateinit var servo: CRServo

    override fun init() {
        servo = hardwareMap[CRServo::class.java, "servo"]
    }

    override fun loop() {
        if(gamepad1.a) servo.power = 1.0
        if(gamepad1.b) servo.power = -1.0
    }
}