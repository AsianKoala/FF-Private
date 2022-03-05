package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Indexer
import com.asiankoala.koawalib.command.commands.WaitCommand

class IndexerCommands {
    open class IndexerCommand(private val indexer: Indexer, private val position: Double, seconds: Double = 0.3) : WaitCommand(seconds) {
        override fun execute() {
            indexer.setPosition(position)
        }

        init {
            addRequirements(indexer)
        }
    }

    class Home(outtake: Indexer) : IndexerCommand(outtake, Indexer.HOME_POSITION)
    class Lock(outtake: Indexer) : IndexerCommand(outtake, Indexer.LOCK_POSITION)
    class Index(outtake: Indexer) : IndexerCommand(outtake, Indexer.INDEX_POSITION)
}
