package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoDepositSequence
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.auto.CycleSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

open class HutaoAuto(private val alliance: Alliance) : CommandOpMode() {
    private val startPose = Pose(16.0, alliance.decide(64.0, -64.0), 0.0)
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        hutao = Hutao(startPose)

        val initialIntakeX = 64.0
        val intakeWaypoints = listOf(
                Waypoint(startPose.x, startPose.y, 8.0,
                        0.0, stop = true, maxMoveSpeed = 0.6,
                        deccelAngle = 40.0.radians, minAllowedHeadingError = 20.0.radians, lowestSlowDownFromHeadingError = 0.2),
                Waypoint(initialIntakeX, startPose.y, 6.0,
                        0.0, stop = true, maxMoveSpeed = 0.6,
                        deccelAngle = 40.0.radians, minAllowedHeadingError = 20.0.radians, lowestSlowDownFromHeadingError = 0.2)
        )

        val depositWaypoints = listOf(
                Waypoint(initialIntakeX, startPose.y, 8.0,
                        0.0, stop = true, maxMoveSpeed = 0.7,
                        deccelAngle = 40.0.radians, minAllowedHeadingError = 20.0.radians, lowestSlowDownFromHeadingError = 0.2),
                Waypoint(startPose.x, startPose.y, 6.0,
                        0.0, stop = true, maxMoveSpeed = 0.7,
                        deccelAngle = 40.0.radians, minAllowedHeadingError = 20.0.radians, lowestSlowDownFromHeadingError = 0.2)
        )

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(alliance, driver.rightTrigger, hutao.outtake,
                        hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                AutoDepositSequence(hutao.slides, hutao.indexer),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer,
                        hutao.arm, hutao.encoders.slideEncoder),

                CycleSequence(
                        alliance,
                        intakeWaypoints,
                        depositWaypoints,
                        hutao.drive,
                        hutao.intake,
                        hutao.outtake,
                        hutao.indexer,
                        hutao.turret,
                        hutao.arm,
                        hutao.slides,
                        hutao.encoders.slideEncoder
                ),

        ).withName("main command")

        mainCommand.schedule()
    }
}

@Autonomous
class AllianceBlue : HutaoAuto(Alliance.BLUE)

@Autonomous
class AllianceRed : HutaoAuto(Alliance.RED)