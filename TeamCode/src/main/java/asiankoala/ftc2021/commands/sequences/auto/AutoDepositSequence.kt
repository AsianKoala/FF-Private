package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class AutoDepositSequence(slides: Slides, indexer: Indexer) : SequentialCommandGroup(
        InstantCommand({slides.generateAndFollowMotionProfile(Slides.autoInches)}, slides),
        WaitCommand( 1.0),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCommand(0.5)
)
