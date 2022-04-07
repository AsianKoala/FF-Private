package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.commands.ResetPoseCommand
import asiankoala.ftc2021.commands.sequences.auto.AutoDepositSequence
import asiankoala.ftc2021.commands.sequences.auto.AutoInitSequence
import asiankoala.ftc2021.commands.sequences.auto.CycleSequence
import asiankoala.ftc2021.commands.sequences.teleop.HomeSequence
import asiankoala.ftc2021.commands.subsystem.IntakeStopperCommands
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.InstantCommand
import com.asiankoala.koawalib.command.commands.PathCommand
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.radians
import com.asiankoala.koawalib.path.Path
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.OpModeState

open class HutaoCycleAuto(private val alliance: Alliance) : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        val startPose = Pose(16.0, alliance.decide(64.0, -64.0), 0.0)
        val depositY = startPose.y
        val depositX = 16.0
        val resetPose = Pose(depositX-2, depositY, 0.0)

        hutao = Hutao(startPose)


        val initialIntakeX = 76.0
        val intakeWaypoints = listOf(
                Waypoint(startPose.x,
                        depositY,
                        12.0,
                        0.0,
                ),
                Waypoint(initialIntakeX,
                        depositY,
                        14.0,
                        0.0,
                        stop = false,
                        deccelAngle = 15.0.radians,
                        minAllowedHeadingError = 10.0.radians,
                        lowestSlowDownFromXError = 0.8,
                        lowestSlowDownFromHeadingError = 0.8
                )
        )

        val depositWaypoints = listOf(
                Waypoint(initialIntakeX,
                        depositY,
                        12.0,
                        0.0,
                ),
                Waypoint(depositX,
                        depositY,
                        8.0,
                        0.0,
                        stop = false,
                        deccelAngle = 15.0.radians,
                        minAllowedHeadingError = 10.0.radians,
                        lowestSlowDownFromXError = 0.8,
                        lowestSlowDownFromHeadingError = 0.8
                )
        )

        val lateDepositWaypoints = listOf(
                Waypoint(initialIntakeX,
                        depositY,
                        12.0,
                        0.0,
                ),
                Waypoint(depositX+3.0,
                        depositY,
                        8.0,
                        0.0,
                        stop = false,
                        deccelAngle = 15.0.radians,
                        minAllowedHeadingError = 10.0.radians,
                        lowestSlowDownFromXError = 0.8,
                        lowestSlowDownFromHeadingError = 0.8
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
                        hutao.encoders.odo,
                        resetPose
                ),

                CycleSequence(
                        alliance,
                        intakeWaypoints,
                        lateDepositWaypoints,
                        hutao.drive,
                        hutao.intake,
                        hutao.outtake,
                        hutao.indexer,
                        hutao.turret,
                        hutao.arm,
                        hutao.slides,
                        hutao.encoders.odo,
                        resetPose
                ),


                CycleSequence(
                        alliance,
                        intakeWaypoints,
                        lateDepositWaypoints,
                        hutao.drive,
                        hutao.intake,
                        hutao.outtake,
                        hutao.indexer,
                        hutao.turret,
                        hutao.arm,
                        hutao.slides,
                        hutao.encoders.odo,
                        resetPose
                ),

                CycleSequence(
                        alliance,
                        intakeWaypoints,
                        lateDepositWaypoints,
                        hutao.drive,
                        hutao.intake,
                        hutao.outtake,
                        hutao.indexer,
                        hutao.turret,
                        hutao.arm,
                        hutao.slides,
                        hutao.encoders.odo,
                        resetPose
                ),

                PathCommand(hutao.drive, Path(intakeWaypoints), 2.0)

                ).withName("main command")

        mainCommand.schedule()
    }
}

