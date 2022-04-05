package asiankoala.ftc2021.commands

import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup

class IntakeSequenceCommand(intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, turretAngle: Double, arm: Arm, waitUntil: () -> Boolean) : SequentialCommandGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCommand(0.2),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCommand(intake::startReading)),
        WaitUntilCommand(waitUntil),
        IntakeCommands.IntakeTurnReverseCommand(intake)
                .alongWith(IndexerCommands.IndexerLockCommand(indexer)),
        WaitCommand(0.5),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(IntakeCommands.IntakeTurnOffCommand(intake), ArmCommands.ArmDepositHighCommand(arm)),
        WaitCommand(0.3),
        InstantCommand({ turret.setPIDTarget(turretAngle) }, turret)
//                .alongWith(ArmCommands.ArmDepositHighCommand(arm)),
) {
    init {
        addRequirements(intake, outtake, indexer, turret, arm)
    }
}