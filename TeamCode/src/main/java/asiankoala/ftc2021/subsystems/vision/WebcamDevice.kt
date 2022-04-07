package asiankoala.ftc2021.subsystems.vision

import com.asiankoala.koawalib.subsystem.DeviceSubsystem
import org.openftc.easyopencv.OpenCvPipeline

class WebcamDevice(private val webcamName: String, private val pipeline: KoawaPipeline) : DeviceSubsystem() {
    val device = Webcam(webcamName, pipeline)

    val cupState get() = pipeline.cupState

    override fun periodic() {
        if (pipeline.LeftTotal > pipeline.MIN_R && pipeline.RightTotal > pipeline.MIN_R && pipeline.CenterTotal > pipeline.MIN_R) {
            pipeline.cupState = CupStates.LEFT
        }

        else if ( pipeline.LeftTotal > pipeline.MIN_R && pipeline.CenterTotal < pipeline.RightTotal) {
            pipeline.cupState = CupStates.CENTER
        }

        else if ( pipeline.RightTotal < pipeline.CenterTotal && pipeline.LeftTotal > pipeline.MIN_R) {
            pipeline.cupState = CupStates.RIGHT
        }

        else {
            pipeline.cupState = CupStates.RIGHT
        }
    }
}