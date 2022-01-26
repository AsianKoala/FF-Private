package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.lib.opmode.BlueAlliance
import robotuprising.lib.opmode.RedAlliance

// todo start coding a rough draft
open class OsirisTeleOp : OsirisOpMode() {


}

@BlueAlliance
@TeleOp
class BlueTeleOp : OsirisTeleOp()

@RedAlliance
@TeleOp
class RedTeleOp : OsirisTeleOp()