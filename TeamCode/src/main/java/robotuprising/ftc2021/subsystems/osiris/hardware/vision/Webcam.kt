package robotuprising.ftc2021.subsystems.osiris.hardware.vision

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.ftc2021.subsystems.osiris.Subsystem

open class Webcam(private val name: String) : Subsystem(), Loopable, Initializable {
    private lateinit var webcam: OpenCvCamera
    private val pipeline = Pipeline()

    override fun init() {
        val cameraMonitorViewId = BulkDataManager.hwMap.appContext.
        resources.getIdentifier("cameraMonitorViewId", "id", BulkDataManager.hwMap.appContext.packageName)

        val webcamName = BulkDataManager.hwMap[WebcamName::class.java, name]
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId)

        webcam.setPipeline(pipeline)

        webcam.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {
                throw Exception("webcam errored")
            }
        })
    }

    override fun loop() {
        if (pipeline.LeftTotal > pipeline.MIN_R && pipeline.RightTotal > pipeline.MIN_R && pipeline.CenterTotal > pipeline.MIN_R) {
            pipeline.cupState = Pipeline.CupStates.LEFT
        } else if ( pipeline.LeftTotal > pipeline.MIN_R && pipeline.CenterTotal < pipeline.RightTotal) {
            pipeline.cupState = Pipeline.CupStates.CENTER
        } else if ( pipeline.RightTotal < pipeline.CenterTotal && pipeline.LeftTotal > pipeline.MIN_R) {
            pipeline.cupState = Pipeline.CupStates.RIGHT
        } else {
            pipeline.cupState = Pipeline.CupStates.RIGHT
        }
    }

    override fun stop() {
        webcam.stopStreaming()
    }

    override fun updateDashboard(debugging: Boolean) {

    }
}
