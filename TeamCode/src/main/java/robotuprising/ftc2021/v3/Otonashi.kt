package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.*
import robotuprising.koawalib.subsystem.odometry.OdoConfig
import kotlin.math.max
import kotlin.math.min

class Otonashi(hardware: Hardware) {
    val drive = Drive(hardware.flMotor, hardware.blMotor, hardware.frMotor, hardware.brMotor,
            OdoConfig(1892.3724, 8.690685, 7.641969,
                    hardware.frMotor, hardware.blMotor, hardware.brMotor, -1.0, -1.0))

    val intake = Intake(hardware.intakeMotor, hardware.loadingSensor)
    val turret = Turret(hardware.turretMotor, hardware.turretLimitSwitch)
    val pitch = Pitch(hardware.pitchMotor, hardware.pitchLimitSwitch)
    val slides = Slides(hardware.slideMotor)

    val indexer = Indexer(hardware.indexerServo)
    val outtake = Outtake(hardware.outtakeServo)

    var hub = Hub.ALLIANCE_HIGH

    fun incrementState() {
        hub = Hub.values()[min(hub.ordinal+1, Hub.values().size-1)]
    }

    fun decrementState() {
        hub = Hub.values()[max(hub.ordinal-1, 0)]
    }
}