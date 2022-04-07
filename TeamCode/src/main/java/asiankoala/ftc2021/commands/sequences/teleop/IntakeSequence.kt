package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.Strategy
import asiankoala.ftc2021.commands.subsystem.*
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.ConditionalCommand
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.util.Logger

class IntakeSequence(strategy: () -> Strategy, intake: Intake, outtake: Outtake, indexer: Indexer, turret: Turret, arm: Arm, slides: Slides, extendShared: () -> Boolean) : SequentialCommandGroup(
        OuttakeCommands.OuttakeHomeCommand(outtake)
                .alongWith(IndexerCommands.IndexerOpenCommand(indexer)),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnOnCommand(intake)
                .alongWith(InstantCommand(intake::startReading)),
        WaitUntilCommand(intake::hasMineral),
        IndexerCommands.IndexerLockCommand(indexer)
                .alongWith(InstantCommand(intake::stopReading)),
        WaitCommand(0.3),
        IntakeCommands.IntakeTurnReverseCommand(intake),
        WaitCommand(0.5),
        InstantCommand({
            val strat = strategy.invoke()
            val pos = if(strat == Strategy.SHARED_BLUE || strat == Strategy.SHARED_RED) {
                Outtake.outtakeSharedPosition
            } else {
                Outtake.outtakeHighPosition
            }
            outtake.setPosition(pos)
        }, outtake)
                .alongWith(InstantCommand({arm.setPosition(strategy.invoke().getArmPosition())}, arm)),
        InstantCommand({
            val strat = strategy.invoke()
            val angle = strat.getTurretAngle()
            turret.setPIDTarget(angle)
        }, turret).alongWith(
                IntakeCommands.IntakeTurnOffCommand(intake),
                ConditionalCommand(
                            InstantCommand({
                                val shouldExtendFarther = extendShared.invoke()
                                slides.generateAndFollowMotionProfile(
                                        if(shouldExtendFarther) {
                                            Slides.sharedExtInches
                                        } else {
                                            Slides.sharedInches
                                        }
                                )
                            }),
                        InstantCommand({})
                ) {
                    val strat = strategy.invoke()
                    strat == Strategy.SHARED_RED || strat == Strategy.SHARED_BLUE
                }
        )
)