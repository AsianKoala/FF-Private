package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.lib.opmode.BlueAlliance
import robotuprising.lib.opmode.RedAlliance

// todo fix pure pursuit tbh
open class OsirisAuto : OsirisOpMode()

@BlueAlliance
@Autonomous
class BlueAuto : OsirisAuto()

@RedAlliance
@Autonomous
class RedAuto : OsirisAuto()