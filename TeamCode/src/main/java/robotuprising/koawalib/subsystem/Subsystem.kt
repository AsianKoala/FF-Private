package robotuprising.koawalib.subsystem

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.CommandScheduler

interface Subsystem {
    fun periodic() {

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