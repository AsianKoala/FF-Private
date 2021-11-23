package robotuprising.ftc2021.opmodes.testing.vision

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvPipeline
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvInternalCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.opencv.core.*
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener


@TeleOp
class HubVisionTest : OpMode() {
    lateinit var phoneCam: OpenCvInternalCamera
    lateinit var pipeline: RingDetectorPipeline

    override fun init() {
        // literally just last yrs code for starting up the camera and initializing everything
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
        phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId)
        pipeline = RingDetectorPipeline()
        phoneCam.setPipeline(pipeline)

        // fixes the problems we had last year with the camera preview on the phone not being in landscape or wtever
        // also starts the camera

        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW)

        phoneCam.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                phoneCam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT)
            }

            override fun onError(errorCode: Int) {

            }
        })
    }

    override fun loop() {
        telemetry.addData("Rings: ", pipeline!!.ringAmount)
        telemetry.update()
    }

    /*
    *
    * Most of the stuff written here was got from the
    * documentation in the opencv library (linked below)
    * easyopencv is a nice lib that allows for use of opencv with ftc without too much bullshit
    *
    * @link https://docs.opencv.org/3.1.0/
    * */
    class RingDetectorPipeline : OpenCvPipeline() {
        enum class RingAmount {
            DEBUG,  // checks if the detector is being stupid
            NONE, ONE, FOUR
        }

        // using the constants we calculate the points actually used for the rectangles
        // point a would be the top left point, point b would be the bottom right (creating a diagonal)
        var four_ring_pointA: Point = FOUR_RING_TOP_LEFT_ANCHOR
        var four_ring_pointB: Point = Point(FOUR_RING_TOP_LEFT_ANCHOR.x + REC_WIDTH, FOUR_RING_TOP_LEFT_ANCHOR.y + REC_HEIGHT)
        var one_ring_pointA: Point = ONE_RING_TOP_LEFT_ANCHOR
        var one_ring_pointB: Point = Point(ONE_RING_TOP_LEFT_ANCHOR.x + REC_WIDTH, ONE_RING_TOP_LEFT_ANCHOR.y + REC_HEIGHT)

        // computation vars
        // region1 is bound by the first the points
        // region2 is bound by the second 2
        var region1_Cb: Mat? = null
        var region2_Cb: Mat? = null
        var YCrCb = Mat()
        var Cb = Mat()
        var avg1 = 0
        var avg2 = 0

        var ringAmount = RingAmount.DEBUG
            private set

        // turns rgb input into YCrCb
        fun rgbToCb(input: Mat?) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb)
            Core.extractChannel(YCrCb, Cb, 2)
        }

        // just makes sure shit is initialized so it doesn't break later on
        override fun init(firstFrame: Mat) {
            rgbToCb(firstFrame)
            region1_Cb = Cb.submat(Rect(four_ring_pointA, four_ring_pointB))
            region2_Cb = Cb.submat(Rect(one_ring_pointA, one_ring_pointB))
        }

        override fun processFrame(input: Mat): Mat {
            /*
            * - Explanation of whats happening here - (I'm basically a god commenter at this point amiright)
            * We convert the colors from RGB to YCrCb
            * This is because the vision code can better view the
            * region without being affected by darkness/brightness since that
            * will be in the Y channel instead of the Cb or Cr channel.
            * In rgb this would've been reflected over all 3 channels, R, G, and B
            * This link was really good at explaining how YCrCb compares to other color spaces
            * It also shows the differences in OpenCV code
            * https://www.learnopencv.com/color-spaces-in-opencv-cpp-python/
            *
            * We only need the Cb channel from the color-space since that reflects
            * the yellow-orange color the best
            * */

            // changes color space
            rgbToCb(input)

            // get avg of each region
            avg1 = Core.mean(region1_Cb).`val`[0].toInt()
            avg2 = Core.mean(region2_Cb).`val`[0].toInt()

            /*
            * draws rectangles on the phone like last year's vision code
            * honestly the most retarded part of the code
            * it took me so long to manually estimate where the rectangles should be
            * until i realized that i could simply make rectangles based on the initial points that i made
            * yeah.. kinda stupid of me
            *
            * since the rectangles will be overlapped i thought its better to make them a separate color/thickness
            */

            // big region for 4 rings
            Imgproc.rectangle(
                    input,  // what to draw on
                    four_ring_pointA,  // top left pt
                    four_ring_pointB,  // bottom right pt
                    BLUE,  // color
                    2
            )
            Imgproc.rectangle(
                    input,
                    one_ring_pointA,
                    one_ring_pointB,
                    GREEN,
                    1 // i guess just make it 1 since its inside of the other region? idk
            )

            // TODO: add rectangle showing correct region on phone
            // TODO: find a threshold for no rings
            val threshold = 0
            if (avg1 > avg2) { // most of the region is yellow-orange
                ringAmount = RingAmount.FOUR
            } else if (avg2 > threshold) { // some (most likely low) part of the region is yellow-orange
                ringAmount = RingAmount.ONE
            } else { // near 0 of the region is yellow-orange (needs to be adjusted with threshold)
                ringAmount = RingAmount.NONE
            }
            return input
        }

        companion object {
            // color constants used for rectangles drawn on the phone like last yr
            val BLUE = Scalar(0.0, 0.0, 255.0)
            val GREEN = Scalar(0.0, 255.0, 0.0)
            val RED = Scalar(255.0, 0.0, 0.0)

            // constants for defining the area of rectangles
            // TODO: actually find values for these constants
            val FOUR_RING_TOP_LEFT_ANCHOR: Point = Point(100.0, 100.0)
            val ONE_RING_TOP_LEFT_ANCHOR: Point = Point(100.0, 50.0)
            const val REC_WIDTH = 20
            const val REC_HEIGHT = 10
        }
    }
}
