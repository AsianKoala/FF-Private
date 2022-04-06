package asiankoala.ftc2021.commands.subsystem

import asiankoala.ftc2021.subsystems.Indexer
import com.asiankoala.koawalib.command.commands.InstantCommand

object IndexerCommands {
    class IndexerOpenCommand(indexer: Indexer) : InstantCommand(indexer::open, indexer)
    class IndexerLockCommand(indexer: Indexer) : InstantCommand(indexer::lock, indexer)
    class IndexerIndexCommand(indexer: Indexer) : InstantCommand(indexer::index, indexer)
}