package asiankoala.ftc2021.commands.sequences.auto

import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.subsystems.*
import com.asiankoala.koawalib.command.commands.PathCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.path.Path
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.util.Alliance

class CycleSequence(alliance: Alliance,
                    intakeWaypoints: List<Waypoint>,
                    depositWaypoints: List<Waypoint>,
                    drive: KMecanumOdoDrive,
                    intake: Intake,
                    outtake: Outtake,
                    indexer: Indexer,
                    turret: Turret,
                    arm: Arm,
                    slides: Slides,
) : SequentialCommandGroup(
        PathCommand(
                drive,
                Path(intakeWaypoints),
                2.0,
        ).raceWith(AutoIntakeSequence(intake)),

        AutoCockSequence(alliance, intake, outtake, indexer, turret, arm)
            .alongWith(PathCommand(drive, Path(depositWaypoints), 2.0)),

        AutoDepositSequence(slides, indexer),
        HomeSequence(turret, slides, outtake, indexer, arm)
)