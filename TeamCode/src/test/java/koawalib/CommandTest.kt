package koawalib

import robotuprising.koawalib.command.commands.Command
import robotuprising.koawalib.command.CommandScheduler

object CommandTest {
    @JvmStatic
    fun main(args: Array<String>) {
        CommandScheduler.resetScheduler()

        val c = Command { println("wow") }
    }
}
