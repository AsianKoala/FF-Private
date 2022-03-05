package asiankoala.junk.v2.lib.control.auto.path

import asiankoala.junk.v2.lib.util.Extensions.deepCopy
import asiankoala.koawalib.path.Waypoint
import java.util.*
import kotlin.collections.ArrayList

object PathUtil {

    fun smooth(waypoints: ArrayList<Waypoint>, weightData: Double, weightSmooth: Double, tolerance: Double): ArrayList<Waypoint> {
        val newPath = waypoints.deepCopy() as ArrayList<Waypoint>

//        var delta = tolerance
//        while (delta >= tolerance) {
//            delta = 0.0
//            for (i in 1 until waypoints.size - 1) {
//                val x_prev = newPath[i].x
//                newPath[i].x += weightData * (waypoints[i].x - newPath[i].x) + weightSmooth * (newPath[i - 1].x + newPath[i + 1].x - (2 * newPath[i].x))
//
//                val y_prev = newPath[i].y
//                newPath[i].y += weightData * (waypoints[i].y - newPath[i].y) + weightSmooth * (newPath[i - 1].y + newPath[i + 1].y - (2 * newPath[i].y))
//
//                delta += (newPath[i].x - x_prev).absoluteValue + (newPath[i].y - y_prev).absoluteValue
//            }
//        }

        return newPath
    }

    fun unsmooth(waypoints: ArrayList<Waypoint>): ArrayList<Waypoint> {
        val li = LinkedList<Waypoint>()

        li.add(waypoints[0])

        for (i in 1 until waypoints.size - 1) {
            val backVector = (waypoints[i].point - waypoints[i - 1].point).atan2
            val forwardVector = (waypoints[i + 1].point - waypoints[i].point).atan2

//            if ((forwardVector - backVector).abs >= 0.01) {
//                li.add(waypoints[i])
//            }
        }

        li.add(waypoints[waypoints.size - 1])

        return li.map { it } as ArrayList<Waypoint>
    }
}
