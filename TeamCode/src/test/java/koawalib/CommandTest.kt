package koawalib

import neil.koawalib.command.commands.Command
import neil.koawalib.command.CommandScheduler

object CommandTest {
    @JvmStatic
    fun main(args: Array<String>) {
        CommandScheduler.resetScheduler()

        val c = Command { println("wow") }
    }
}
