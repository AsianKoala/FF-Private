package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.DistanceSensor
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.I2cAddr
import com.qualcomm.robotcore.hardware.I2cDeviceSynch
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType
import com.qualcomm.robotcore.util.TypeConversion
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

@I2cDeviceType
@DeviceProperties(name = "MB1242", description = "ultrasonic distance sensor", xmlTag = "MB1242")
class MB1242(deviceClient: I2cDeviceSynch?) : I2cDeviceSynchDevice<I2cDeviceSynch?>(deviceClient, true), DistanceSensor {
    override fun getManufacturer(): Manufacturer {
        return Manufacturer.Unknown
    }

    @Synchronized
    override fun doInitialize(): Boolean {
        return true
    }

    override fun getDeviceName(): String {
        return "MB1242 I2C Ultrasonic Distance Sensor"
    }

    fun ping() {
        deviceClient!!.write(0xE0, TypeConversion.intToByteArray(0x51))
    }

    private fun readRawRange(): Short {
        return TypeConversion.byteArrayToShort(deviceClient!!.read(0xE1, 2))
    }

    /**
     * Allow 100ms between pinging. Returns in centimeters
     */
    override fun getDistance(unit: DistanceUnit): Double {
        return unit.fromCm(readRawRange().toDouble())
    }

    init {
        this.deviceClient!!.i2cAddress = I2cAddr.create8bit(0xe1)
        super.registerArmingStateCallback(false)
        this.deviceClient!!.engage()
    }
}
