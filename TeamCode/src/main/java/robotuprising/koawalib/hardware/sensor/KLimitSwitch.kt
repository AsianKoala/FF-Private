package robotuprising.koawalib.hardware.sensor

import com.qualcomm.robotcore.hardware.DigitalChannel
import robotuprising.koawalib.hardware.HardwareDevice
import robotuprising.koawalib.util.interfaces.KBoolean

class KLimitSwitch : HardwareDevice<DigitalChannel>, KBoolean {
    constructor(device:  DigitalChannel) : super(device)
    constructor(name: String) : super(name)

    override fun invokeBoolean(): Boolean {
        return device.state
    }
}