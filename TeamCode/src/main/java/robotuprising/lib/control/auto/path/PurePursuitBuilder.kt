package robotuprising.lib.control.auto.path

import robotuprising.lib.control.auto.waypoints.LockedWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import kotlin.math.PI

class PurePursuitBuilder : PathBuilder() {
    val path: ArrayList<Waypoint> = ArrayList()
    fun addPoint(p: Waypoint): PurePursuitBuilder {
        path.add(p)
        return this
    }

    fun reverse(): PurePursuitBuilder {
        for (w in path) {
            w.x = -w.x
            if (w is LockedWaypoint) {
                w.h += Angle(PI, AngleUnit.RAD)
            }
        }
        return this
    }

    override fun build(): Path = PurePursuitPath(path)
}
