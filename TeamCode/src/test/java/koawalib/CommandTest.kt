package koawalib

import asiankoala.koawalib.command.CommandScheduler
import asiankoala.koawalib.command.commands.Command

object CommandTest {
    @JvmStatic
    fun main(args: Array<String>) {
        CommandScheduler.resetScheduler()

        val c = Command { println("wow") }
    }
}
