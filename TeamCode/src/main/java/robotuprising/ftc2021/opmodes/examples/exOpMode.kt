package robotuprising.ftc2021.opmodes.examples

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import robotuprising.ftc2021.opmodes.examples.exIntake

class exOpMode : OpMode() {
    private var bIntake: exIntake? = null
    override fun init() {
        bIntake = exIntake(hardwareMap)
    }

    override fun loop() {
        bIntake!!.turnIntakeOn()
        bIntake!!.distanceSensorReading
    }
}