package neil.koawalib.hardware.sensor

import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import neil.koawalib.hardware.HardwareDevice
import neil.koawalib.util.KDouble

class KRev2mDistanceSensor: HardwareDevice<Rev2mDistanceSensor>, KDouble {
    constructor(device: Rev2mDistanceSensor) : super(device)
    constructor(name: String) : super(name)

    override fun invokeDouble(): Double {
        return device.getDistance(DistanceUnit.MM)
    }
}