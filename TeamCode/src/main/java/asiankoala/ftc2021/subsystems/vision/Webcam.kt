package asiankoala.ftc2021.subsystems.vision

import com.asiankoala.koawalib.hardware.KDevice
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.*

class Webcam(deviceName: String, pipeline: OpenCvPipeline) : KDevice<WebcamName>(deviceName) {
    private val webcam: OpenCvWebcam

    fun startStreaming() {
        webcam.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {

            }
        })
    }

    fun stopStreaming() {
        webcam.stopStreaming()
    }

    init {
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        webcam = OpenCvCameraFactory.getInstance().createWebcam(device, cameraMonitorViewId)
        webcam.setPipeline(pipeline)
    }
}