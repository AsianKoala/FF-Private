package robotuprising.lib.hardware

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.I2cDeviceSynch
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType

@I2cDeviceType
@DeviceProperties(name = "MB1242 Ultrasonic Sensor", description = "ULTRASONIC SENSOR", xmlTag = "MB1242")
class MB1242(deviceClient: I2cDeviceSynch) : I2cDeviceSynchDevice<I2cDeviceSynch>(deviceClient, true) {
    override fun getManufacturer(): HardwareDevice.Manufacturer {
        TODO("Not yet implemented")
    }

    override fun getDeviceName(): String {
        TODO("Not yet implemented")
    }

    override fun doInitialize(): Boolean {
        TODO("Not yet implemented")
    }

    enum class Register {

    }

    init {
        this.deviceClient.engage()
    }
}