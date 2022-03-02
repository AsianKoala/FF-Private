package neil.koawalib.hardware.sensor

import com.qualcomm.robotcore.hardware.DigitalChannel
import neil.koawalib.hardware.HardwareDevice
import neil.koawalib.util.KBoolean

class KLimitSwitch : HardwareDevice<DigitalChannel>, KBoolean {
    constructor(device:  DigitalChannel) : super(device)
    constructor(name: String) : super(name)

    override fun invokeBoolean(): Boolean {
        return device.state
    }
}