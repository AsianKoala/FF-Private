package robotuprising.ftc2021.opmodes.testing

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

@TeleOp
class UltrasonicsTest : OpMode() {
    private lateinit var ultrasonic: Ultrasonics

    override fun init() {
        ultrasonic = Ultrasonics()
    }

    override fun start() {
        ultrasonic.startReading()
    }

    override fun loop() {
        ultrasonic.update()

//        telemetry.addData("ultrasonic reading", ultrasonic.getForwardRange(DistanceUnit.MM))
        telemetry.addData("counter", ultrasonic.counter)
        telemetry.update()
    }

    override fun stop() {
        ultrasonic.stopReading()
    }
}