package neil.koawalib.subsystem

import neil.koawalib.command.commands.Command
import neil.koawalib.command.CommandScheduler
import neil.koawalib.util.Periodic

interface Subsystem : Periodic {

    override fun periodic() {

    }

    fun setDefaultCommand(command: Command) {
        CommandScheduler.setDefaultCommand(this, command)
    }

    fun getDefaultCommand(): Command {
        return CommandScheduler.getDefaultCommand(this)
    }

    fun getCurrentCommand(): Command {
        return CommandScheduler.requiring(this)
    }

    fun register() {
        CommandScheduler.registerSubsystem(this)
    }
}