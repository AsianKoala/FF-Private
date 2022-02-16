package robotuprising.koawalib.hardware.motor

import com.qualcomm.robotcore.hardware.DcMotorEx
import robotuprising.koawalib.hardware.HardwareDeviceGroup

class MotorGroup<T : DcMotorEx>(vararg motors: Motor<T>) : Motor<T>(motors[0].device), HardwareDeviceGroup<Motor<T>> {

    override fun getFollowers(): Array<Motor<T>> {
        TODO("Not yet implemented")
    }

    override fun getAllDevices(): Array<Motor<T>> {
        TODO("Not yet implemented")
    }

    init {

    }
}