package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.ParallelCommandGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import kotlin.math.absoluteValue

class HomeSequence(turret: Turret, slides: Slides, outtake: Outtake, indexer: Indexer, arm: Arm, slideEncoder: KEncoder) : SequentialCommandGroup(
        ParallelCommandGroup(
                IndexerCommands.IndexerLockCommand(indexer),
                OuttakeCommands.OuttakeCockCommand(outtake),
                ArmCommands.ArmHomeCommand(arm)
        ),
        WaitCommand(0.2),
        InstantCommand({slides.generateAndFollowMotionProfile(Slides.slideHomeValue)}),
        WaitCommand(0.5),
        InstantCommand({turret.setPIDTarget(Turret.turretHomeValue)}),
        WaitUntilCommand { slideEncoder.position.absoluteValue < 2.0},
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer))
) {
    init {
        addRequirements(turret, slides, outtake, indexer, arm)
    }
}