package robotuprising.ftc2021.opmodes.testing
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubServo

@TeleOp(name = "Servo Programmer")
class ServoProgrammer : OpMode() {
    private lateinit var ourServoData: ServoData
    private var prevTime = System.currentTimeMillis()

    override fun init() {
        val servo = hardwareMap.get(ExpansionHubServo::class.java, "outtakeLeft") // ex
        ourServoData = ServoData(servo, 0.05)
    }

    override fun loop() {
        if (System.currentTimeMillis() - prevTime > 300) {
            prevTime = System.currentTimeMillis()
            telemetry.addLine(ourServoData.update(gamepad1.x, gamepad1.b, gamepad1.a))
        }
    }
}

private class ServoData(private val servo: ExpansionHubServo, private val increment: Double) {
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
        return servo.deviceName + ": " + servoPos
    }
}
