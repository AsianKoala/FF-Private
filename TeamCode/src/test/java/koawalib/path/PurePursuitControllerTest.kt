package koawalib.path

import robotuprising.koawalib.math.MathUtil.radians
import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.path2.PurePursuitController

object PurePursuitControllerTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val currPose = Pose(5.0, 5.0, 45.0.radians)
        val targetPosition = Point(10.0, 10.0)

        val result = PurePursuitController.goToPosition(currPose, targetPosition, stop = true)

        println(result)
    }
}
