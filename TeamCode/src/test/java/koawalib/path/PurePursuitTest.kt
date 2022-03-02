package koawalib.path

import neil.koawalib.math.MathUtil.radians
import neil.koawalib.math.Pose
import neil.koawalib.path.Path
import neil.koawalib.path.PurePursuitController
import neil.koawalib.path.Waypoint

object PurePursuitTest {

    @JvmStatic
    fun main(args: Array<String>) {
        testPurePursuit()
    }

    val waypoints = listOf(
            Waypoint(),
            Waypoint(30.0, 10.0, 10.0),
            Waypoint(50.0, 50.0, 10.0, stop = false)
    )

    val position = Pose(23.0, 12.0, 18.0.radians)

    fun testCalcLookahead() {
        val lookahead = PurePursuitController.calcLookahead(waypoints, position,10.0)
    }

    fun testPurePursuit() {
        val path = Path(waypoints)
        path.update(position)
    }
}
