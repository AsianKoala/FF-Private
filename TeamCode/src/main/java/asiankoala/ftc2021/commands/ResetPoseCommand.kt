package asiankoala.ftc2021.commands

import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.group.ParallelCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.subsystem.drive.KMecanumOdoDrive
import com.asiankoala.koawalib.subsystem.odometry.KThreeWheelOdometry

class ResetPoseCommand(odometry: KThreeWheelOdometry, pose: Pose) : ParallelCommandGroup(
        InstantCommand({odometry.pose = pose}),
        InstantCommand(odometry::reset)
)