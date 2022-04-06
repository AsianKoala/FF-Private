package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.port.Path
import asiankoala.port.PathCommand
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.command.commands.WaitUntilCommand
import com.asiankoala.koawalib.command.group.SequentialCommandGroup
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.path.Waypoint
import com.asiankoala.koawalib.util.Logger
import com.asiankoala.koawalib.util.LoggerConfig
import com.asiankoala.koawalib.util.OpModeState
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class PathTestAuto : CommandOpMode() {
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false,
                isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        val startPose = Pose(16.0, 64.0, 0.0)
        hutao = Hutao(startPose)

        val initialIntakeX = 56.0

        mainCommand = SequentialCommandGroup(
                WaitUntilCommand { opmodeState == OpModeState.LOOP },
                PathCommand(
                        hutao.drive,
                        Path(listOf(
                                Waypoint(16.0, 64.0, 8.0, 0.0, stop = true, maxMoveSpeed = 0.7,),
                                Waypoint(56.0, 64.0, 8.0, 0.0, stop = true, maxMoveSpeed = 0.7),
                        )),
                        2.0
                ),

                PathCommand(
                        hutao.drive,
                        Path(listOf(
                                Waypoint(56.0, 64.0, 8.0, 0.0, stop = true, maxMoveSpeed = 0.7,),
                                Waypoint(16.0, 64.0, 8.0, 0.0, stop = true, maxMoveSpeed = 0.7),
                        )),
                        2.0
                )
        ).withName("main command")

        mainCommand.schedule()
    }

    override fun mLoop() {
        if(!mainCommand.isScheduled) {
            requestOpModeStop()
        }

        hutao.log()
    }
}