package robotuprising.koawalib.subsystem.sensors

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.koawalib.hardware.HardwareDevice
import robotuprising.koawalib.util.KDouble

class KRevColorSensor  : HardwareDevice<RevColorSensorV3>, KDouble {
    constructor(device: RevColorSensorV3) : super(device)
    constructor(name: String) : super(name)

    override fun invokeDouble(): Double {
        return device.getDistance(DistanceUnit.MM)
    }
}