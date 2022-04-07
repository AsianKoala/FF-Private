package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.util.Logger
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class SlidesMaxInchTest : CommandOpMode() {
    private lateinit var hutao: Hutao

    override fun mInit() {
        hutao = Hutao(Pose())
        hutao.slides.unregister()
    }

    override fun mLoop() {
        hutao.encoders.slideEncoder.update()
        Logger.addTelemetryData("slide inches", hutao.encoders.slideEncoder.position)
    }
}