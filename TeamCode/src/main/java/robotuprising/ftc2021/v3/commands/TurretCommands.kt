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

    class Home(turret: Turret) :  TurretCommand(turret, Turret.homeAngle)
    class Shared(turret: Turret) : TurretCommand(turret, Turret.sharedAngle)
    class Alliance(turret: Turret) : TurretCommand(turret, Turret.allianceAngle)
}
