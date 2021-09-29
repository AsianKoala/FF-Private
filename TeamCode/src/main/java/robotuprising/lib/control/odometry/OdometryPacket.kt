package robotuprising.lib.control.odometry

abstract class OdometryPacket {
    abstract var currLeftEncoder: Int
    abstract var currRightEncoder: Int
    abstract var currPerpEncoder: Int
}