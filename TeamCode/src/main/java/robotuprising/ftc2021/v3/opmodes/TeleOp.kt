package robotuprising.ftc2021.v3.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.v3.Controls
import robotuprising.ftc2021.v3.Hardware
import robotuprising.ftc2021.v3.Rin
import robotuprising.koawalib.structure.CommandOpMode

@TeleOp
class TeleOp : CommandOpMode() {
    override fun mInit() {
        val hardware = Hardware()
        val rin = Rin(hardware)
        val controls = Controls(rin, driverGamepad, gunnerGamepad)
        controls.bindControls()
    }
}