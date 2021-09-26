package robotuprising.lib.movement.odometry

import robotuprising.lib.math.Pose

interface OdometryInt {
    val TICKS_PER_INCH: Double
    val TURN_SCALAR: Double
    val PERP_SCALAR: Double
    fun update(odometryPacketImpl: OdometryPacketAbs): Pose
}