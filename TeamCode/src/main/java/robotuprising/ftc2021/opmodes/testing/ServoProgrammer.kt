package robotuprising.ftc2021.opmodes.testing
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubServo

@TeleOp(name = "Servo Programmer")
@Disabled
class ServoProgrammer : OpMode() {
    private lateinit var sLeftData: ServoData
    private lateinit var sRightData: ServoData
    private var prevTime = System.currentTimeMillis()
    val servo1Name = "outtakeLeft"
    val servo2Name = "outtakeRight"

    /*

    outtakeLeft:
    >=0.5 goes to robot
    0.0 out
    0.35 in

    outtakeRight:
    <=0.5 goes to robot
    0.60 out
    0.25 in


     */

    override fun init() {
        val servoLeft = hardwareMap[ExpansionHubServo::class.java, servo1Name]
        val servoRight = hardwareMap[ExpansionHubServo::class.java, servo2Name]
        sLeftData = ServoData(servoLeft, 0.05, servo1Name)
        sRightData = ServoData(servoRight, 0.05, servo2Name)
    }

    override fun loop() {
        if (System.currentTimeMillis() - prevTime > 300) {
            prevTime = System.currentTimeMillis()
            telemetry.addLine(sLeftData.update(gamepad1.x, gamepad1.b, gamepad1.a))
            telemetry.addLine(sRightData.update(gamepad1.dpad_left, gamepad1.dpad_right, gamepad1.dpad_down))
        }
    }
}

private class ServoData(private val servo: ExpansionHubServo, private val increment: Double, private val name: String) {
    private var servoPos = 0.5
    fun update(isDecrease: Boolean, isIncrease: Boolean, set: Boolean): String {
        if (isDecrease) {
            servoPos = Range.clip(servoPos - increment, 0.0, 1.0)
        } else if (isIncrease) {
            servoPos = Range.clip(servoPos + increment, 0.0, 1.0)
        }
        if (set) {
            servo.position = servoPos
        }
        return "$name: $servoPos"
    }
}
