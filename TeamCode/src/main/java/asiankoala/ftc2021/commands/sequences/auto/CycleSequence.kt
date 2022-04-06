package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.GoToPointCommand
import com.asiankoala.koawalib.command.group.ParallelRaceGroup
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.util.Alliance

class CycleSequence(alliance: Alliance,
                    drive: KMecanumOdoDrive,
                    initialIntakeX: Double,
                    intake: Intake,
                    outtake: Outtake,
                    indexer: Indexer,
                    turret: Turret,
                    arm: Arm,
                    depositPose: Pose,
                    slides: Slides,
                    slideEncoder: KEncoder
) : SequentialCommandGroup(
        GoToPointCommand(
                drive,
                Pose(initialIntakeX, alliance.decide(64.0, -64.0), 0.0),
                distTol = 1.5,
                angleTol = 1.5.radians,
                stop = true,
        ).raceWith(AutoIntakeSequence(intake)),
        AutoCockSequence(alliance, intake, outtake, indexer, turret, arm)
                .alongWith(
                        GoToPointCommand(
                                drive,
                                depositPose,
                                1.5,
                                1.5.radians,
                                stop = true
                        )
                ),
        AutoDepositSequence(slides, indexer),
        HomeSequence(turret, slides, outtake, indexer, arm, slideEncoder),
)