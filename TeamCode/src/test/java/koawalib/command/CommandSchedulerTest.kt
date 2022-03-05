package koawalib.command

import asiankoala.koawalib.command.CommandScheduler
import asiankoala.koawalib.command.commands.*

object CommandSchedulerTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val array = mutableListOf(0, 0, 0, 0)
        val first = InlineCommand(mOnExecute = { array[0]++ }, mIsFinished = { array[0] >= 5 })
        val second = InlineCommand(mOnExecute = { array[1] += 5 }, mIsFinished = { array[1] >= 11 })
        val third = InlineCommand(mOnExecute = { array[2] += 3 }, mIsFinished = { array[2] >= 14 })

        val fourth = InlineCommand(mOnExecute = { array[3] += 4 }, mIsFinished = { array[3] >= 16 })
            .deadlineWith(first, second, third)

//        val c = ParallelRaceGroup(first, second, third)
        CommandScheduler.resetScheduler()
//        c.schedule()

        fourth.schedule()
        while (CommandScheduler.mScheduledCommands.isNotEmpty()) {
            CommandScheduler.run()
            println(array)
        }

        CommandScheduler.printTelemetry()
    }
}
