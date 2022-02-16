package robotuprising.koawalib.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice

class DummyDevice<T>(private val internal: T) : HardwareDevice {

    override fun getManufacturer(): HardwareDevice.Manufacturer {
        return HardwareDevice.Manufacturer.Unknown
    }

    override fun getDeviceName(): String {
        return internal.toString()
    }

    override fun getConnectionInfo(): String {
        return "L + ratio"
    }

    override fun getVersion(): Int {
        return 0
    }

    override fun resetDeviceConfigurationForOpMode() {

    }

    override fun close() {

    }

    fun get(): T {
        return internal
    }
}