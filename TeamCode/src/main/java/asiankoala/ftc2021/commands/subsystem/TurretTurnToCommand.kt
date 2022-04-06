package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.command.commands.InstantCommand

class TurretTurnToCommand(turret: Turret, angle: Double) : InstantCommand({ turret.setPIDTarget(angle) }, turret)
