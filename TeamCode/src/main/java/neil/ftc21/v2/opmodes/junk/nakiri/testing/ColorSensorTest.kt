package neil.ftc21.v2.opmodes.junk.nakiri.testing

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

@TeleOp
@Disabled
class ColorSensorTest : OpMode() {

    lateinit var intakeSensor: RevColorSensorV3

    var noneRgb = Triple(0, 0, 0)
    var ballRgb = Triple(0, 0, 0)
    var cubeRgb = Triple(0, 0, 0)

    val RevColorSensorV3.rgb get() = Triple(red(), green(), blue())

    override fun init() {
        intakeSensor = hardwareMap[RevColorSensorV3::class.java, "intakeSensor"]
    }

    override fun loop() {
        when {
            gamepad1.a -> noneRgb = intakeSensor.rgb
            gamepad1.b -> ballRgb = intakeSensor.rgb
            gamepad1.x -> cubeRgb = intakeSensor.rgb
        }

        telemetry.addData("rgb", intakeSensor.rgb)
        telemetry.addData("distance", intakeSensor.getDistance(DistanceUnit.MM))
        telemetry.addData("normalized colors", intakeSensor.normalizedColors.toColor())
        telemetry.addData("argb", intakeSensor.argb())
        telemetry.update()
    }
}
