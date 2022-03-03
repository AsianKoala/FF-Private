package asiankoala.ftc21.v2.lib.control.auto.path

import asiankoala.koawalib.path.Waypoint
import asiankoala.ftc21.v2.lib.math.Pose
import kotlin.collections.ArrayList

abstract class Path(val waypoints: ArrayList<Waypoint>) {
    var currWaypoint: Int = 0
        protected set

    val isFinished get() = currWaypoint >= waypoints.size - 1

    abstract fun update(currPose: Pose): Pose

    init {
        val copy = ArrayList<Waypoint>()
        waypoints.forEach { copy.add(it.copy) }
        waypoints.clear()
        waypoints.addAll(copy)
    }
}
