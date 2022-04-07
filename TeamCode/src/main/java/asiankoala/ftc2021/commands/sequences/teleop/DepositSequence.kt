package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.subsystems.Indexer
import asiankoala.ftc2021.subsystems.Slides
import com.asiankoala.koawalib.command.commands.ConditionalCommand
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Logger

class DepositSequence(strategy: () -> Strategy, slides: Slides, indexer: Indexer, continueDeposit: () -> Boolean) : SequentialCommandGroup(
        ConditionalCommand(
                InstantCommand({}),
                InstantCommand({
                    val strat = strategy.invoke()
                    val inches = strat.getSlideInches()
                    slides.generateAndFollowMotionProfile(inches)
                }, slides)
        ) {
            val strat = strategy.invoke()
            strat == Strategy.SHARED_RED || strat == Strategy.SHARED_BLUE
        },
        WaitCommand( 0.5),
        WaitUntilCommand(continueDeposit),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCommand(0.8)
)
