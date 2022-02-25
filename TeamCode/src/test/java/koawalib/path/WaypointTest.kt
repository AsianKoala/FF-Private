package koawalib.path

import robotuprising.koawalib.path2.Waypoint

object WaypointTest {
    @JvmStatic
    fun main(args: Array<String>) {
        var waypoint = Waypoint()
        waypoint = waypoint.copy(x = 1.0, y = 1.0)

        println("${waypoint.x}, ${waypoint.y}")
    }
}
