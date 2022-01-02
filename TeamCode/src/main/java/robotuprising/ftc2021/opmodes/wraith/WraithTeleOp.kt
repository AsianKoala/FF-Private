package robotuprising.ftc2021.opmodes.wraith

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.lib.opmode.BlueAlliance
import robotuprising.lib.opmode.RedAlliance

open class WraithTeleOp : Robot() {
    
}

@BlueAlliance
@TeleOp
class BlueTeleOp : WraithTeleOp()

@RedAlliance
@TeleOp
class RedTeleOp : WraithTeleOp()