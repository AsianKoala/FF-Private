package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.subsystem.IndexerCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.*
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.angleWrap
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.*
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class HutaoDuckBlue : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        val startPose = Pose(-32.0, 64.0, 180.0.radians)
        hutao = Hutao(startPose)
        val duckSpinPose = Pose(-60.0, 56.0, 180.0.radians)

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(Alliance.RED, driver.rightTrigger, hutao.outtake, hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                InstantCommand({hutao.turret.setPIDTarget(115.0)}),
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
                                listOf(
                                        NormalWaypoint(
                                                startPose.x,
                                                startPose.y,
                                                8.0
                                        ),

                                        NormalWaypoint(
                                                duckSpinPose.x,
                                                duckSpinPose.y,
                                                10.0,
                                                duckSpinPose.heading,
                                                maxMoveSpeed = 0.3,
                                                30.0.radians,
                                                stop = true,
                                        )

                                )
                        ),
                        2.0
                ),
                InstantCommand({hutao.duck.setSpeed(0.25)}),
                WaitCommand(4.0),
                InstantCommand({hutao.duck.setSpeed(0.0)}),
                NormalPathCommand(
                        hutao.drive,
                        NormalPath(
                                listOf(
                                        NormalWaypoint(
                                                duckSpinPose.x,
                                                duckSpinPose.y,
                                                10.0
                                        ),

                                        NormalWaypoint(
                                                -64.0,
                                                40.0,
                                                8.0,
                                                270.0.radians,
                                                stop = true
                                        )
                                )
                        ),
                        2.0
                ),
            )

        mainCommand.schedule()
    }
}