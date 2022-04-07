package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.sequences.auto.AutoDepositSequence
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.auto.CycleSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.subsystem.IntakeStopperCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState

open class HutaoCycleAuto(private val alliance: Alliance) : CommandOpMode() {
    private val startPose = Pose(16.0, alliance.decide(64.0, -64.0), 0.0)
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        hutao = Hutao(startPose)

        val yOffset = alliance.decide(0.0, 0.0)
        val initialIntakeX = 64.0
        val intakeWaypoints = listOf(
                Waypoint(startPose.x,
                        startPose.y + yOffset,
                        8.0,
                        0.0,
                ),
                Waypoint(initialIntakeX,
                        startPose.y + yOffset,
                        8.0,
                        0.0,
                        stop = false,
                        deccelAngle = 20.0.radians,
                        minAllowedHeadingError = 10.0.radians,
                        minAllowedXError = 2.5,
                        lowestSlowDownFromXError = 0.2
                )
        )

        val depositWaypoints = listOf(
                Waypoint(initialIntakeX,
                        startPose.y + yOffset,
                        8.0,
                        0.0,
                ),
                Waypoint(startPose.x - 2.0,
                        startPose.y + yOffset,
                        8.0,
                        0.0,
                        stop = true,
                        deccelAngle = 20.0.radians,
                        minAllowedHeadingError = 10.0.radians,
                        minAllowedXError = 2.5,
                        lowestSlowDownFromXError = 0.2
                )
        )

        mainCommand = SequentialCommandGroup(
                AutoInitSequence(alliance, driver.rightTrigger, hutao.outtake,
                        hutao.arm, hutao.turret, hutao.indexer),
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                AutoDepositSequence(hutao.slides, hutao.indexer),
                HomeSequence(hutao.turret, hutao.slides, hutao.outtake, hutao.indexer,
                        hutao.arm),

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
                ),

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
                ),

                InstantCommand(hutao.encoders.odo::reset),

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
                ),

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
                ),

                InstantCommand(hutao.encoders.odo::reset),

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
                ),

                ).withName("main command")

        mainCommand.schedule()
    }

    override fun mLoop() {
        hutao.log()
    }
}

