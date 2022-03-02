package neil.koawalib.hardware.sensor

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import neil.koawalib.hardware.HardwareDevice
import neil.koawalib.util.KDouble

class KRevColorSensor  : HardwareDevice<RevColorSensorV3>, KDouble {
    constructor(device: RevColorSensorV3) : super(device)
    constructor(name: String) : super(name)

    override fun invokeDouble(): Double {
        return device.getDistance(DistanceUnit.MM)
    }
}