package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.commands.subsystem.IntakeStopperCommands
import asiankoala.ftc2021.commands.subsystem.OuttakeCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.gamepad.functionality.Button
import com.asiankoala.koawalib.util.Alliance

/**
 * this runs in init to ready the robot for auto
 * assume the preload has been placed, and the turret is zeroed.
 * the outtake goes to cock, indexer is locked, arm is
 */
class AutoInitSequence(alliance: Alliance, readyButton: Button, outtake: Outtake, arm: Arm, turret: Turret, indexer: Indexer, intakeStopper: IntakeStopper) : SequentialCommandGroup(
        IndexerCommands.IndexerLockCommand(indexer),
        WaitUntilCommand(readyButton::isJustPressed),
        IntakeStopperCommands.IntakeStopperLockCommand(intakeStopper),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(ArmCommands.ArmDepositHighCommand(arm)),
        InstantCommand({ turret.setPIDTarget(alliance.decide(Turret.blueAngle, Turret.redAngle))}),
)
