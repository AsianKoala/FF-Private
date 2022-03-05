package lib

import asiankoala.junk.v2.lib.control.odometry.OdometryPacket

class OdometryPacketImpl : OdometryPacket() {
    override var currLeftEncoder: Int = 1
    override var currRightEncoder: Int = 2
    override var currPerpEncoder: Int = 3
}

object TestOdometryPacketImpl {
    @JvmStatic
    fun main(args: Array<String>) {
        val asd = OdometryPacketImpl()
        asd.currLeftEncoder = 5
        println(asd.currLeftEncoder)
    }
}
