package robotuprising.ftc2021.v3.commands

import robotuprising.ftc2021.v3.subsystems.*
import robotuprising.koawalib.command.commands.WaitCommand
import robotuprising.koawalib.command.commands.WaitUntilCommand
import robotuprising.koawalib.command.group.ParallelCommandGroup
import robotuprising.koawalib.command.group.SequentialCommandGroup

object DepositCommands {
    class DepositHigh(
            condition: () -> Boolean,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            ParallelCommandGroup(
                TurretCommands.Alliance(turret),
                PitchCommands.AllianceHigh(pitch),
                SlideCommands.AllianceHigh(slides),
            ),
            DepositThenHome(condition, indexer, outtake, turret, pitch, slides)
    )

    class DepositMid(
            depositCondition: () -> Boolean,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            ParallelCommandGroup(
                    TurretCommands.Alliance(turret),
                    PitchCommands.AllianceMid(pitch),
                    SlideCommands.AllianceMid(slides),
            ),
            DepositThenHome(depositCondition, indexer, outtake, turret, pitch, slides)
    )

    class DepositLow(
            condition: () -> Boolean,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            ParallelCommandGroup(
                    TurretCommands.Alliance(turret),
                    PitchCommands.AllianceLow(pitch),
                    SlideCommands.AllianceLow(slides),
            ),
            DepositThenHome(condition, indexer, outtake, turret, pitch, slides)
    )

    class DepositThenHome(
            condition: () -> Boolean,
            indexer: Indexer,
            outtake: Outtake,
            turret: Turret,
            pitch: Pitch,
            slides: Slides
    ) : SequentialCommandGroup(
            WaitUntilCommand(condition),
            IndexerCommands.Index(indexer).pauseFor(0.5),
            IndexerCommands.Home(indexer),
            OuttakeCommands.Home(outtake),
            TurretCommands.Home(turret)
                    .alongWith(
                            PitchCommands.Home(pitch),
                            SlideCommands.Home(slides)
                    )
    )
}
