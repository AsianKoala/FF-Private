package robotuprising.lib.hardware

import com.qualcomm.robotcore.hardware.*
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType

@I2cDeviceType
@DeviceProperties(name = "MB1242 Ultrasonic Sensor", description = "ULTRASONIC SENSOR", xmlTag = "MB1242")
class MB1242(deviceClient: I2cDeviceSynch) : I2cDeviceSynchDevice<I2cDeviceSynch>(deviceClient, true) {

    fun poll() {
        val data = ByteArray(1)
        data[0] = 0x51.toByte()
        deviceClient.write(0xE0, data)
    }

    fun getDistance(): Int {
        val read = deviceClient.read(0xE1)
        val range_low_byte = read[0]
        val range_high_byte = read[1]
        return range_high_byte * 256 + range_low_byte
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer {
        return HardwareDevice.Manufacturer.Other
    }

    override fun getDeviceName(): String {
        return "MB1242 Ultrasonic Sensor"
    }

    @Synchronized
    override fun doInitialize(): Boolean {
        return true
    }

    init {
        super.registerArmingStateCallback(false)
        deviceClient.i2cAddress = I2cAddr.create7bit(0x70)
        deviceClient.engage()

    }
}