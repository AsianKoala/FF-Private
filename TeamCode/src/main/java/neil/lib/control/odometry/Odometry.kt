package neil.lib.control.odometry

import neil.lib.math.Pose

interface Odometry {
    val TICKS_PER_INCH: Double
    val TURN_SCALAR: Double
    val PERP_SCALAR: Double
    fun update(odometryPacket: OdometryPacket): Pose
}
