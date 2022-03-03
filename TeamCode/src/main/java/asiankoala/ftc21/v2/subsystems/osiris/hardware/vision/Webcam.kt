package asiankoala.ftc21.v2.subsystems.osiris.hardware.vision

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import asiankoala.ftc21.v2.hardware.osiris.interfaces.Initializable
import asiankoala.ftc21.v2.hardware.osiris.interfaces.Loopable
import asiankoala.ftc21.v2.manager.BulkDataManager
import asiankoala.ftc21.v2.subsystems.osiris.Subsystem
import asiankoala.ftc21.v2.lib.opmode.OsirisDashboard

open class Webcam(private val name: String) : Subsystem(), Loopable, Initializable {
    private lateinit var webcam: OpenCvCamera
    val pipeline = Pipeline()

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
        OsirisDashboard["CUP STATE"] = pipeline.cupState
    }
}
