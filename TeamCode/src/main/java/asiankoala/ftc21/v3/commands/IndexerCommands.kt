package asiankoala.ftc21.v3.commands

import com.asiankoala.koawalib.command.commands.WaitCommand
import asiankoala.ftc21.v3.subsystems.Indexer

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