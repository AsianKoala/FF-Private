package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.NormalPath
import com.asiankoala.koawalib.path.NormalPathCommand
import com.asiankoala.koawalib.path.NormalWaypoint
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class HutaoDuckRed : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        val startPose = Pose(-32.0, -64.0, 180.0.radians)
        val duckPose = Pose(-63.0, -55.0, 270.0.radians)
        hutao = Hutao(startPose)

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(Alliance.BLUE, driver.rightTrigger, hutao.outtake, hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                InstantCommand({hutao.turret.setPIDTarget(240.0)}),
                WaitCommand(0.3),
                InstantCommand({hutao.slides.generateAndFollowMotionProfile(35.0)}),
                WaitCommand(1.5),
                IndexerCommands.IndexerIndexCommand(hutao.indexer),
                WaitCommand(0.5),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer, hutao.arm, hutao.intake),
                WaitCommand(0.5),
                NormalPathCommand(
                        hutao.drive,
                        NormalPath(
                                NormalWaypoint(
                                        startPose.x,
                                        startPose.y,
                                        8.0
                                ),

                                NormalWaypoint(
                                        startPose.x - 12.0,
                                        startPose.y + 22.0,
                                        8.0,
                                        headingLockAngle = 180.0.radians
                                ),

                                NormalWaypoint(
                                        duckPose.x,
                                        duckPose.y,
                                        8.0,
                                        headingLockAngle = duckPose.heading,
                                        stop = true,
                                        maxMoveSpeed = 0.3,
                                ),
                        ),

                        3.0
                ),

                InstantCommand({hutao.duck.setSpeed(-0.3)}),
                WaitCommand(4.0),
                InstantCommand({hutao.duck.setSpeed(0.0)}),
                NormalPathCommand(
                        hutao.drive,
                        NormalPath(
                                NormalWaypoint(
                                        duckPose.x,
                                        duckPose.y,
                                        8.0
                                ),

                                NormalWaypoint(
                                        -64.0,
                                        -35.0,
                                        8.0,
                                        270.0.radians,
                                        stop = true
                                )
                        ),
                        2.0
                )
        )

        mainCommand.schedule()
    }
}