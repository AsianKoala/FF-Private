package robotuprising.ftc2021.v3.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.v3.Controls
import robotuprising.ftc2021.v3.Hardware
import robotuprising.ftc2021.v3.Rin
import robotuprising.koawalib.structure.CommandOpMode

@TeleOp
class TeleOp : CommandOpMode() {

    private lateinit var hardware: Hardware
    private lateinit var rin: Rin
    private lateinit var controls: Controls

    override fun mInit() {
        hardware = Hardware()
        rin = Rin(hardware)
        controls = Controls(rin, driverGamepad, gunnerGamepad)
        controls.bindControls()
    }
}