package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.ParallelCommandGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class ResetAfterDepositCommand(turret: Turret, slides: Slides, outtake: Outtake, indexer: Indexer, arm: Arm) : SequentialCommandGroup(
        ParallelCommandGroup(
                IndexerCommands.IndexerOpenCommand(indexer),
                OuttakeCommands.OuttakeCockCommand(outtake),
                ArmCommands.ArmHomeCommand(arm)
        ),
        WaitCommand(0.5),
        InstantCommand({slides.generateAndFollowMotionProfile(Slides.slideHomeValue)}),
        WaitCommand(0.5),
        InstantCommand({turret.setPIDTarget(Turret.turretHomeValue)}),
        WaitCommand(0.3),
        OuttakeCommands.OuttakeHomeCommand(outtake)
) {
    init {
        addRequirements(turret, slides, outtake, indexer, arm)
    }
}