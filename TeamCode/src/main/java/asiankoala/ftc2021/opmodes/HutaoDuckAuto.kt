package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.math.Pose

class DuckBlue : CommandOpMode() {
    private val startPose = Pose()
    private lateinit var hutao: Hutao
    private lateinit var mainCommand: Command

    override fun mInit() {
        hutao = Hutao(startPose)
        mainCommand.schedule()
    }
}