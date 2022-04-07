package asiankoala.ftc2021.subsystems.vision

import org.opencv.core.Mat
import org.openftc.easyopencv.OpenCvPipeline

abstract class KoawaPipeline : OpenCvPipeline() {
    var workingMatrix = Mat()

    var cupState = CupStates.RIGHT

    var MIN_R = 60

    var LeftTotal : Double = 0.0
    var CenterTotal : Double = 0.0
    var RightTotal : Double = 0.0
}