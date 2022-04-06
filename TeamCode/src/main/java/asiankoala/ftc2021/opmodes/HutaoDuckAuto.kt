package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger
import com.asiankoala.koawalib.util.LoggerConfig

class DuckBlue : CommandOpMode() {
    private val startPose = Pose()
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        Logger.config = LoggerConfig(isLogging = true, isPrinting = false,
            isLoggingTelemetry = false, isDebugging = false, maxErrorCount = 1)
        hutao = Hutao(startPose)
        mainCommand.schedule()
    }
}