package koawalib.path

import neil.koawalib.math.MathUtil.radians
import neil.koawalib.math.Point
import neil.koawalib.math.Pose
import neil.koawalib.path.PurePursuitController

object PurePursuitControllerTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val currPose = Pose(5.0, 5.0, 45.0.radians)
        val targetPosition = Point(10.0, 10.0)

        val result = PurePursuitController.goToPosition(currPose, targetPosition, stop = true)

        println(result)
    }
}
