package robotuprising.koawalib.subsystem

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.util.Periodic

interface Subsystem : Periodic {

    fun register() {
        TODO()
    }

    var defaultCommand: Command
        get() {
            TODO()
        }

        set(value) {
            TODO()
        }

    override fun periodic() {
        TODO("Not yet implemented")
    }
}