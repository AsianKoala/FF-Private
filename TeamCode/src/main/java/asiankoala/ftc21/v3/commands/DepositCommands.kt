package asiankoala.ftc21.v3.commands

import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.ParallelCommandGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import asiankoala.ftc21.v3.subsystems.*

object DepositCommands {
    class DepositHigh(
            condition: () -> Boolean,
            intake: Intake,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            InstantCommand(intake::removeMineral, intake),
            ParallelCommandGroup(
                TurretCommands.Alliance(turret),
                PitchCommands.AllianceHigh(pitch),
                SlideCommands.AllianceHigh(slides),
            ),
            DepositThenHome(condition, indexer, outtake, turret, pitch, slides)
    )

    class DepositMid(
            depositCondition: () -> Boolean,
            intake: Intake,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            InstantCommand(intake::removeMineral, intake),
            ParallelCommandGroup(
                    TurretCommands.Alliance(turret),
                    PitchCommands.AllianceMid(pitch),
                    SlideCommands.AllianceMid(slides),
            ),
            DepositThenHome(depositCondition, indexer, outtake, turret, pitch, slides)
    )

    class DepositLow(
            condition: () -> Boolean,
            intake: Intake,
            turret: Turret,
            pitch: Pitch,
            slides: Slides,
            outtake: Outtake,
            indexer: Indexer
    ) : SequentialCommandGroup(
            InstantCommand(intake::removeMineral, intake),
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
