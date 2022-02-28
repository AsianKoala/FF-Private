package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.Turret
import robotuprising.koawalib.command.commands.CommandBase

object TurretCommands {
    open class TurretCommand(private val turret: Turret, private val angle: Double) : CommandBase() {
        override fun execute() {
            turret.setTurretAngle(angle)
        }

        override val isFinished: Boolean
            get() = turret.isAtTarget

        init {
            addRequirements(turret)
        }
    }

    class HomeCommand(turret: Turret) :  TurretCommand(turret, Turret.homeAngle)
    class SharedCommand(turret: Turret) : TurretCommand(turret, Turret.sharedAngle)
    class AllianceCommand(turret: Turret) : TurretCommand(turret, Turret.allianceAngle)
}
