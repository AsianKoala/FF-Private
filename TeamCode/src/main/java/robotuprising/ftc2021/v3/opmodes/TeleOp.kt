package robotuprising.ftc2021.v3.opmodes

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import robotuprising.ftc2021.v3.Controls
import robotuprising.ftc2021.v3.Hardware
import robotuprising.ftc2021.v3.Robot
import robotuprising.koawalib.structure.CommandOpMode

@TeleOp
class TeleOp : CommandOpMode(Controls(Robot(Hardware())))