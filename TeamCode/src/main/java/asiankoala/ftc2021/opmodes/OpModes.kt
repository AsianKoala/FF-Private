package asiankoala.ftc2021.opmodes

import com.asiankoala.koawalib.util.Alliance
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name="\uD83D\uDD35 Blue TeleOp")
class HutaoBlueTeleOp : HutaoTeleOp(Alliance.BLUE)

@TeleOp(name="\ud83d\udfe5 Red TeleOp")
class HutaoRedTeleOp : HutaoTeleOp(Alliance.RED)

@Autonomous(name="\uD83D\uDD35 ♻️ Blue Cycle Auto")
class HutaoBlueCycleAuto : HutaoCycleAuto(Alliance.BLUE)

@Autonomous(name="\ud83d\udfe5 ♻️ Red Cycle Auto")
class HutaoRedCycleAuto : HutaoCycleAuto(Alliance.RED)