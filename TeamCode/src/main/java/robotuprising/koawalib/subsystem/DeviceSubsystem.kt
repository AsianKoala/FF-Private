package robotuprising.koawalib.subsystem

import robotuprising.koawalib.command.CommandScheduler

open class DeviceSubsystem : Subsystem {
    init {
        CommandScheduler.registerSubsystem(this)
    }
}