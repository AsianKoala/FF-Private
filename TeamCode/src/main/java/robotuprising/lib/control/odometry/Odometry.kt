package robotuprising.lib.control.odometry

import robotuprising.lib.math.Pose

interface Odometry {
    val TICKS_PER_INCH: Double
    val TURN_SCALAR: Double
    val PERP_SCALAR: Double
    fun update(odometryPacket: OdometryPacket): Pose
}