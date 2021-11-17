package robotuprising.lib.hardware

import android.graphics.Color
import androidx.annotation.ColorInt
import com.qualcomm.robotcore.hardware.HardwareDevice.Manufacturer
import com.qualcomm.robotcore.hardware.I2cAddr
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple
import com.qualcomm.robotcore.hardware.I2cWaitControl
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType

@I2cDeviceType
@DeviceProperties(name = "QWIIC LED Stick", description = "Sparkfun QWIIC LED Stick", xmlTag = "QWIIC_LED_STICK")
class QwiicLEDStick(deviceClient: I2cDeviceSynchSimple?) : I2cDeviceSynchDevice<I2cDeviceSynchSimple?>(deviceClient, true) {
    private enum class Commands(var bVal: Int) {
        CHANGE_LED_LENGTH(0x70), WRITE_SINGLE_LED_COLOR(0x71), WRITE_ALL_LED_COLOR(0x72), WRITE_RED_ARRAY(0x73), WRITE_GREEN_ARRAY(0x74), WRITE_BLUE_ARRAY(0x75), WRITE_SINGLE_LED_BRIGHTNESS(0x76), WRITE_ALL_LED_BRIGHTNESS(0x77), WRITE_ALL_LED_OFF(0x78);
    }

    /**
     * Change the color of a specific LED
     *
     * @param position which LED to change (1 - 255)
     * @param color    what color to set it to
     */
    fun setColor(position: Int, @ColorInt color: Int) {
        val data = ByteArray(4)
        data[0] = position.toByte()
        data[1] = Color.red(color).toByte()
        data[2] = Color.green(color).toByte()
        data[3] = Color.blue(color).toByte()
        writeI2C(Commands.WRITE_SINGLE_LED_COLOR, data)
    }

    /**
     * Change the color of all LEDs to a single color
     *
     * @param color what the color should be
     */
    fun setColor(@ColorInt color: Int) {
        val data = ByteArray(3)
        data[0] = Color.red(color).toByte()
        data[1] = Color.green(color).toByte()
        data[2] = Color.blue(color).toByte()
        writeI2C(Commands.WRITE_ALL_LED_COLOR, data)
    }

    /**
     * Send a segment of the LED array
     *
     * @param cmd    command to send
     * @param array  the values (limited from 0..255)
     * @param offset the starting value (LED only, array starts at 0)
     * @param length the length to send
     */
    private fun sendSegment(cmd: Commands, array: IntArray, offset: Int, length: Int) {
        val data = ByteArray(length + 2)
        data[0] = length.toByte()
        data[1] = offset.toByte()
        for (i in 0 until length) {
            data[2 + i] = array[i].toByte()
        }
        writeI2C(cmd, data)
    }

    /**
     * Change the color of an LED color segment
     *
     * @param colors what the colors should be
     * @param offset where in the array to start
     * @param length length to send (limited to 12)
     */
    private fun setLEDColorSegment(@ColorInt colors: IntArray, offset: Int, length: Int) {
        val redArray = IntArray(length)
        val greenArray = IntArray(length)
        val blueArray = IntArray(length)
        for (i in colors.indices) {
            redArray[i] = Color.red(colors[i + offset])
            greenArray[i] = Color.green(colors[i + offset])
            blueArray[i] = Color.blue(colors[i + offset])
        }
        sendSegment(Commands.WRITE_RED_ARRAY, redArray, offset, length)
        sendSegment(Commands.WRITE_GREEN_ARRAY, greenArray, offset, length)
        sendSegment(Commands.WRITE_BLUE_ARRAY, blueArray, offset, length)
    }

    /**
     * Change the color of all LEDs using arrays
     *
     * @param colors array of colors to set lights to
     */
    fun setColors(@ColorInt colors: IntArray) {
        val length = colors.size
        val numInLastSegment = length % 12
        val numSegments = length / 12
        for (i in 0 until numSegments) {
            setLEDColorSegment(colors, i * 12, 12)
        }
        setLEDColorSegment(colors, numSegments * 12, numInLastSegment)
    }

    /**
     * Set the brightness of an individual LED
     *
     * @param number     which LED to change (1-255)
     * @param brightness brightness level (0-31)
     */
    fun setBrightness(number: Int, brightness: Int) {
        val data = ByteArray(2)
        data[0] = number.toByte()
        data[1] = brightness.toByte()
        writeI2C(Commands.WRITE_SINGLE_LED_BRIGHTNESS, data)
    }

    /**
     * Set the brightness of all LEDs
     *
     * @param brightness brightness level (0-31)
     */
    fun setBrightness(brightness: Int) {
        val data = ByteArray(1)
        data[0] = brightness.toByte()
        writeI2C(Commands.WRITE_ALL_LED_BRIGHTNESS, data)
    }

    /**
     * Turn all LEDS off...
     */
    fun turnAllOff() {
        setColor(0)
    }

    /**
     * Change the length of the LED strip
     *
     * @param newLength 1 to 100 (longer than 100 not supported)
     */
    fun changeLength(newLength: Int) {
        val data = ByteArray(1)
        data[0] = newLength.toByte()
        writeI2C(Commands.CHANGE_LED_LENGTH, data)
    }

    private fun writeI2C(cmd: Commands, data: ByteArray) {
        deviceClient!!.write(cmd.bVal, data, I2cWaitControl.WRITTEN)
    }

    override fun getManufacturer(): Manufacturer {
        return Manufacturer.Other
    }

    @Synchronized
    override fun doInitialize(): Boolean {
        return true
    }

    override fun getDeviceName(): String {
        return "Qwiic LED Strip"
    }

    companion object {
        private val ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(0x23)
    }

    init {
        this.deviceClient!!.i2cAddress = ADDRESS_I2C_DEFAULT
        super.registerArmingStateCallback(false)
    }
}
