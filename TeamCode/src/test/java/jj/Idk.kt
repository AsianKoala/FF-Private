package jj

import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.path.Path
import com.asiankoala.koawalib.path.PurePursuitController
import com.asiankoala.koawalib.path.Waypoint

object Idk {
    @JvmStatic
    fun main(args: Array<String>) {
        val pose = Pose(12.0, -64.0, 0.0)
        val waypoints = listOf(
                Waypoint(pose.x, pose.y, 8.0),
                Waypoint(pose.x+24.0, pose.y, 8.0)
        )
        val path = Path(waypoints)
        val powers = path.update(pose, 2.0)
        println(powers)
    }
}
