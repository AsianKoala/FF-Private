package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.hardware.rev.RevColorSensorV3
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ColorSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.lib.util.Extensions.d

@TeleOp
class ColorSensorTest : OpMode() {

    lateinit var intakeSensor: RevColorSensorV3

    var noneRgb = Triple(0, 0, 0)
    var ballRgb = Triple(0,0, 0)
    var cubeRgb = Triple(0, 0, 0)

    val RevColorSensorV3.rgb get() = Triple(red(), green(), blue())

    override fun init() {
        intakeSensor = hardwareMap[RevColorSensorV3::class.java,"intakeSensor"]
    }

    override fun loop() {
        when {
            gamepad1.a -> noneRgb = intakeSensor.rgb
            gamepad1.b -> ballRgb = intakeSensor.rgb
            gamepad1.x -> cubeRgb = intakeSensor.rgb
        }

        telemetry.addData("noneRgb", noneRgb)
        telemetry.addData("ballRgb", ballRgb)
        telemetry.addData("cubeRgb", cubeRgb)
        telemetry.addData("distance", intakeSensor.getDistance(DistanceUnit.MM))
        telemetry.update()
    }
}