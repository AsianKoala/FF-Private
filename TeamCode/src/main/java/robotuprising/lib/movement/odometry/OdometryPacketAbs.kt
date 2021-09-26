package robotuprising.lib.movement.odometry

abstract class OdometryPacketAbs {
    abstract var currLeftEncoder: Int
    abstract var currRightEncoder: Int
    abstract var currPerpEncoder: Int
}