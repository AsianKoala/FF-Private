package asiankoala.ftc2021.opmodes

import com.asiankoala.koawalib.util.Alliance
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="Blue")
class HutaoBlueTeleOp : HutaoTeleOp(Alliance.BLUE)

@TeleOp(name="Red")
class HutaoRedTeleOp : HutaoTeleOp(Alliance.RED)

@Autonomous(name="♻️😈💀 Blue Cycle Auto", preselectTeleOp = "Blue")
class HutaoBlueCycleAuto : HutaoCycleAuto(Alliance.BLUE)

@Autonomous(name="♻️😈💀 Red Cycle Auto", preselectTeleOp = "Red")
class HutaoRedCycleAuto : HutaoCycleAuto(Alliance.RED)

@Autonomous
class VisionBlue : HutaoVisionTest(Alliance.BLUE)

@Autonomous
class VisionRed : HutaoVisionTest(Alliance.RED)