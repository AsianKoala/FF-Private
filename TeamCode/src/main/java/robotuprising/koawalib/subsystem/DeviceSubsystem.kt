package robotuprising.koawalib.subsystem

import robotuprising.koawalib.hardware.HardwareDevice

abstract class DeviceSubsystem<T : HardwareDevice<*>>(protected val device: T) : Subsystem {
    fun exposeDevice(): T {
        return device
    }
}