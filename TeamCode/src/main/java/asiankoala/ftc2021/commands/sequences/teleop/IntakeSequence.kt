package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.IntakeCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Alliance

class IntakeSequence(alliance: Alliance, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm) : SequentialCommandGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCommand(0.2),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCommand(intake::startReading)),
        WaitUntilCommand(intake::hasMineral),
        IndexerCommands.IndexerLockCommand(indexer)
                .alongWith(InstantCommand(intake::stopReading)),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCommand(0.5),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(ArmCommands.ArmDepositHighCommand(arm)),
        WaitCommand(0.3),
        InstantCommand({ turret.setPIDTarget(alliance.decide(Turret.blueAngle, Turret.redAngle)) }, turret)
                .alongWith(IntakeCommands.IntakeTurnOffCommand(intake))
)