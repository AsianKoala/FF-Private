package org.firstinspires.ftc.teamcode.control.path.builders

import org.firstinspires.ftc.teamcode.control.path.Path
import org.firstinspires.ftc.teamcode.control.path.purepursuit.PurePursuitPath
import org.firstinspires.ftc.teamcode.control.path.waypoints.LockedWaypoint
import org.firstinspires.ftc.teamcode.control.path.waypoints.Waypoint
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
