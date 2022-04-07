package asiankoala.ftc2021.commands.sequences.teleop

import asiankoala.ftc2021.commands.subsystem.ArmCommands
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.gamepad.functionality.Button
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.util.Alliance

class DepositSharedSequence(alliance: Alliance, turret: Turret, arm: Arm, indexer: Indexer,
                            slides: Slides, depositButton: Button, intake: Intake, outtake: Outtake, encoder: KEncoder) : SequentialCommandGroup(
        InstantCommand({turret.setPIDTarget(alliance.decide(Turret.sharedBlueAngle, Turret.sharedRedAngle))})
                .alongWith(
                        ArmCommands.ArmDepositSharedCommand(arm),
                        InstantCommand({slides.generateAndFollowMotionProfile(Slides.sharedInches)}, slides)
                ),
        WaitCommand(0.5),
        WaitUntilCommand(depositButton::isJustPressed),
        IndexerCommands.IndexerIndexCommand(indexer),
        WaitCommand(0.5),
        HomeSequence(turret, slides, outtake, indexer, arm)
)