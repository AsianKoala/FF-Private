package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.subsystem.*
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Logger

class IntakeSequence(strategy: () -> Strategy, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm) : SequentialCommandGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCommand(0.4),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCommand(intake::startReading)),
        WaitUntilCommand(intake::hasMineral),
        IndexerCommands.IndexerLockCommand(indexer)
                .alongWith(InstantCommand(intake::stopReading)),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCommand(0.5),
        InstantCommand({ Logger.logInfo("strategy", strategy.invoke())}),
        OuttakeCommands.OuttakeDepositHighCommand(outtake)
                .alongWith(InstantCommand({arm.setPosition(strategy.invoke().getArmPosition())}, arm)),
        WaitCommand(0.3),
        InstantCommand({
            val strat = strategy.invoke()
            val angle = strat.getTurretAngle()
            Logger.logInfo("TARGET ANGLE BRUFAFKF", angle)
            Logger.logInfo("CURRENT STRATEGY", strat)
            turret.setPIDTarget(angle)
        }, turret).alongWith(IntakeCommands.IntakeTurnOffCommand(intake)),
//        TurretTurnToCommand(turret, strategy.invoke().getTurretAngle())
//                .alongWith(IntakeCommands.IntakeTurnOffCommand(intake))
)