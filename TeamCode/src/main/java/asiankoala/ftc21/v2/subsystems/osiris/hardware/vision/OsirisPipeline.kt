package asiankoala.ftc21.v2.subsystems.osiris.hardware.vision

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline

abstract class OsirisPipeline : OpenCvPipeline() {
    var workingMatrix = Mat()

    val MIN_R: Int = 60

    var leftTotal: Double = 0.0
    var centerTotal: Double = 0.0
    var rightTotal : Double = 0.0

    abstract var leftRowStart: Int
    abstract var centerRowStart: Int
    abstract var rightRowStart: Int

    abstract var leftColStart: Int
    abstract var centerColStart: Int
    abstract var rightColStart: Int

    abstract var leftWidth: Int
    abstract var leftHeight: Int
    abstract var centerWidth: Int
    abstract var centerHeight: Int
    abstract var rightWidth: Int
    abstract var rightHeight: Int

    var cupState = CupStates.LEFT
    enum class CupStates {
        LEFT, CENTER, RIGHT
    }

    override fun processFrame(input: Mat): Mat {
        input.copyTo(workingMatrix)

        if(workingMatrix.empty()) {
            return input
        }

        val matLeft = workingMatrix.submat(leftRowStart, leftRowStart + leftHeight,
                leftColStart, leftColStart + leftWidth)
        val matCenter = workingMatrix.submat(centerRowStart, centerRowStart + centerHeight,
                centerColStart, centerColStart + centerWidth)
        val matRight = workingMatrix.submat(rightRowStart, rightRowStart + rightHeight,
                rightColStart, rightColStart + rightWidth)

        leftTotal =  Core.mean(matLeft).`val`[2]
        centerTotal = Core.mean(matCenter).`val`[2]
        rightTotal = Core.mean(matRight).`val`[2]

        Imgproc.cvtColor(workingMatrix, workingMatrix, Imgproc.COLOR_RGB2YCrCb)

        Imgproc.rectangle(workingMatrix, Rect(leftColStart, leftRowStart, leftWidth, leftHeight), Scalar(255.0, 0.0, 0.0))
        Imgproc.rectangle(workingMatrix, Rect(centerColStart, centerRowStart, centerWidth, centerHeight), Scalar(255.0, 0.0, 0.0))
        Imgproc.rectangle(workingMatrix, Rect(rightColStart, rightRowStart, rightWidth, rightHeight), Scalar(255.0, 0.0, 0.0))


        return workingMatrix
    }
}