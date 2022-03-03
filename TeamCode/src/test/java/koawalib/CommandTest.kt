package koawalib

import asiankoala.koawalib.command.commands.Command
import asiankoala.koawalib.command.CommandScheduler

object CommandTest {
    @JvmStatic
    fun main(args: Array<String>) {
        CommandScheduler.resetScheduler()

        val c = Command { println("wow") }
    }
}
