package robotuprising.ftc2021.subsystems.vision

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.Subsystem

class Webcam : Subsystem {
    private lateinit var webcam: OpenCvCamera
    private lateinit var pipeline: CupPipeline

    var cupState = CupStates.LEFT
        private set
    enum class CupStates {
        LEFT, MIDDLE, RIGHT
    }

    fun init() {
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

    // todo
    // i trust myself on making sure that webcam isn't used after start()
    override fun update() {
        val area = pipeline.getRectArea()
        val midpoint = pipeline.getRectMidpointX()

        cupState = when(Globals.ALLIANCE_SIDE) {
            AllianceSide.BLUE -> when {
                area < CupPipeline.MIN_AREA -> CupStates.LEFT
                midpoint > 200 -> CupStates.RIGHT
                else -> CupStates.MIDDLE
            }

            AllianceSide.RED -> when {
                area < CupPipeline.MIN_AREA -> CupStates.RIGHT
                midpoint > 200 -> CupStates.MIDDLE
                else -> CupStates.LEFT
            }
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {

    }

    override fun reset() {
        webcam.stopStreaming()
    }
}