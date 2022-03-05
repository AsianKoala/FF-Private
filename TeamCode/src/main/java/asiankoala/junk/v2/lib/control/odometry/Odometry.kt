package asiankoala.junk.v2.lib.control.odometry

import asiankoala.junk.v2.lib.math.Pose

interface Odometry {
    val TICKS_PER_INCH: Double
    val TURN_SCALAR: Double
    val PERP_SCALAR: Double
    fun update(odometryPacket: OdometryPacket): Pose
}
