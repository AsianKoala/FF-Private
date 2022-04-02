package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class DepositCommand(slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialCommandGroup(
        InstantCommand({slides.generateAndFollowMotionProfile(Slides.slideDepositInches)}, slides),
        WaitUntilCommand(continueDeposit),
        IndexerCommands.IndexerOpenCommand(indexer)
)