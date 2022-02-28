package robotuprising.ftc2021.v3.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.v3.Controls
import robotuprising.ftc2021.v3.Hardware
import robotuprising.ftc2021.v3.Otonashi
import robotuprising.koawalib.command.CommandOpMode

@TeleOp
class TeleOp : CommandOpMode() {
    override fun mInit() {
        val hardware = Hardware()
        val otonashi = Otonashi(hardware)
        val controls = Controls(otonashi, driverGamepad, gunnerGamepad)
        controls.bindControls()
    }
}