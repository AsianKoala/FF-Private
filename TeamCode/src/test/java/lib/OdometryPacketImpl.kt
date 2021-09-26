package lib

import robotuprising.lib.movement.odometry.OdometryPacketAbs

class OdometryPacketImpl : OdometryPacketAbs() {
    override var currLeftEncoder: Int = 1
    override var currRightEncoder: Int = 2
    override var currPerpEncoder: Int = 3
}

object t {
    @JvmStatic
    fun main(args: Array<String>) {
        val asd = OdometryPacketImpl()
        asd.currLeftEncoder = 5
        println(asd.currLeftEncoder)
    }
}
