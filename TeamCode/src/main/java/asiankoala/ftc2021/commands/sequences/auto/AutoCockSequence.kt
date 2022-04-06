package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.subsystem.*
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.command.group.ParallelCommandGroup

class AutoCockSequence(alliance: Alliance, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm) : SequentialCommandGroup(
        IndexerCommands.IndexerLockCommand(indexer),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCommand(0.5),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(ArmCommands.ArmDepositHighCommand(arm)),
        WaitCommand(0.3),
        TurretTurnToCommand(turret, alliance.decide(Turret.blueAngle, Turret.redAngle))
                .alongWith(IntakeCommands.IntakeTurnOffCommand(intake))
)