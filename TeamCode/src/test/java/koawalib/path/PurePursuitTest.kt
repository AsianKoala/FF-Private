package koawalib.path

import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.path2.Path
import robotuprising.koawalib.path2.PurePursuitController
import robotuprising.koawalib.path2.Waypoint

object PurePursuitTest {
    @JvmStatic
    fun main(args: Array<String>) {
        testLineCircleIntersection()
    }

    fun testLineCircleIntersection() {
        val start = Point()
        val end = Point(20,20)
        val center = Point(10,10)
        val radius = 5.0
        val intersections = PurePursuitController.lineCircleIntersection(center, start, end, radius)
        for(int in intersections) {
            println(int)
        }
    }

    fun testAlgo() {
        val waypoints = listOf(
                Waypoint(),
                Waypoint(10.0, 20.0, 8.0),
                Waypoint(50.0, 30.0, 8.0)
        )

        val path = Path(waypoints)

        path.update(Pose(0.0, 0.0, 0.0))
    }
}
