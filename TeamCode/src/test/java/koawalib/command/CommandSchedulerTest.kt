package koawalib.command

import robotuprising.koawalib.command.commands.*
import robotuprising.koawalib.command.CommandScheduler

object CommandSchedulerTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val array = mutableListOf(0,0,0,0)
        val first = FunctionalCommand(mOnExecute = { array[0]++ }, mIsFinished = { array[0] >= 5 })
        val second = FunctionalCommand(mOnExecute = { array[1]+=5 }, mIsFinished = { array[1] >= 11 })
        val third = FunctionalCommand(mOnExecute = { array[2]+=3 }, mIsFinished = { array[2] >= 14 })

        val fourth = FunctionalCommand(mOnExecute = { array[3]+=4 }, mIsFinished = { array[3] >= 16 })
                .deadlineWith(first, second, third)

//        val c = ParallelRaceGroup(first, second, third)
        CommandScheduler.resetScheduler()
//        c.schedule()

        fourth.schedule()
        while(CommandScheduler.mScheduledCommands.isNotEmpty()) {
            CommandScheduler.run()
            println(array)
        }

        CommandScheduler.printTelemetry()
    }
}
