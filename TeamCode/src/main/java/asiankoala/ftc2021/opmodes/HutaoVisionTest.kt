package asiankoala.ftc2021.opmodes

import asiankoala.ftc2021.Hutao
import asiankoala.ftc2021.subsystems.vision.BluePipeline
import asiankoala.ftc2021.subsystems.vision.CupStates
import asiankoala.ftc2021.subsystems.vision.RedPipeline
import asiankoala.ftc2021.subsystems.vision.WebcamDevice
import com.asiankoala.koawalib.command.CommandOpMode
import com.asiankoala.koawalib.command.commands.Command
import com.asiankoala.koawalib.util.Alliance
import com.asiankoala.koawalib.util.Logger
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

open class HutaoVisionTest(private val alliance: Alliance) : CommandOpMode() {
    private var cupState: CupStates? = null
    private lateinit var webcam: WebcamDevice

    override fun mInit() {
        webcam = WebcamDevice(alliance.decide("blueWebcam", "redWebcam"), alliance.decide(BluePipeline(), RedPipeline()))
        webcam.device.startStreaming()
    }

    override fun mStart() {
        cupState = webcam.cupState
        webcam.device.stopStreaming()
    }

    override fun mLoop() {
        Logger.addTelemetryData("cup state", cupState)
    }
}

