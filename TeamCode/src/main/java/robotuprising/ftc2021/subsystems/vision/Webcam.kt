package robotuprising.ftc2021.subsystems.vision

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.lib.system.Subsystem

class Webcam : Subsystem {
    private var webcam: OpenCvCamera
    private var pipeline: CupPipeline

    private var cupState = CupStates.LEFT
    private enum class CupStates {
        LEFT, MIDDLE, RIGHT
    }

    // todo
    // i trust myself on making sure that webcam isn't used after start()
    override fun update() {
        val area = pipeline.getRectArea()
        val midpoint = pipeline.getRectMidpointX()

        cupState = when {
            area < CupPipeline.MIN_AREA -> CupStates.LEFT
            midpoint > 200 -> CupStates.RIGHT
            else -> CupStates.MIDDLE
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {

    }

    override fun reset() {
        webcam.stopStreaming()
    }

    init {
        val hardwareMap = BulkDataManager.hwMap
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        val webcamName = hardwareMap[WebcamName::class.java, "Webcam"]
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId)

        pipeline = CupPipeline()
        webcam.setPipeline(pipeline)

        webcam.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {

            }
        })
    }
}