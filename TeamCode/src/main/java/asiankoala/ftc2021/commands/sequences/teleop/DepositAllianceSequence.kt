package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class DepositAllianceSequence(slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialCommandGroup(
        InstantCommand({slides.generateAndFollowMotionProfile(Slides.slideDepositInches)}, slides),
        WaitCommand( 0.5),
        WaitUntilCommand(continueDeposit),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCommand(0.5)
)
