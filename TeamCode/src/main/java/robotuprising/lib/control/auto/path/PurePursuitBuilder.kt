package robotuprising.lib.control.auto.path

import robotuprising.koawalib.path2.Waypoint

class PurePursuitBuilder(val path: ArrayList<Waypoint> = ArrayList()) : PathBuilder() {
    fun addPoint(p: Waypoint): PurePursuitBuilder {
        path.add(p)
        return this
    }

    fun reverse(): PurePursuitBuilder {
//        for (w in path) {
//            w.x = -w.x
//            if (w is LockedWaypoint) {
//                w.h += Angle(PI, AngleUnit.RAD)
//            }
//        }
        return this
    }

    override fun build(): Path = PurePursuitPath(path)
}
