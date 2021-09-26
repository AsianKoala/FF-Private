package robotuprising.lib.movement.path

import robotuprising.lib.movement.waypoints.Waypoint
import robotuprising.ftc2021.deprecated.OldAzusa
import kotlin.collections.ArrayList

abstract class Path(val waypoints: ArrayList<Waypoint>) {
    var currWaypoint: Int = 0
        protected set

    val isFinished get() = currWaypoint >= waypoints.size - 1

    abstract fun update(azusa: OldAzusa)

    init {
        val copy = ArrayList<Waypoint>()
        waypoints.forEach { copy.add(it.copy) }
        waypoints.clear()
        waypoints.addAll(copy)
    }
}
