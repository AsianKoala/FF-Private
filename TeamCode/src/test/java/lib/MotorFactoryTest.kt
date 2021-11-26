package lib

object MotorFactoryTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val motor = NakiriMotorFactory.name("fl").master.openLoopControl.brake.forward
    }
}
