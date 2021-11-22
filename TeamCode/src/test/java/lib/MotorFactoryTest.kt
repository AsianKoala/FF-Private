package lib

import robotuprising.ftc2021.util.NakiriMotorFactory

object MotorFactoryTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val motor = NakiriMotorFactory.name("fl").master.openLoopControl.brake.forward
    }
}
