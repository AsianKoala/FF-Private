package asiankoala.ftc21.v3

import com.asiankoala.koawalib.subsystem.odometry.OdoConfig
import asiankoala.ftc21.v3.subsystems.*
import kotlin.math.max
import kotlin.math.min

class Otonashi(hardware: Hardware) {
    val drive = Drive(hardware.flMotor, hardware.blMotor, hardware.frMotor, hardware.brMotor,
            OdoConfig(1892.3724, 8.690685, 7.641969,
                    hardware.odoLeftEncoder, hardware.odoRightEncoder, hardware.odoAuxEncoder)
    )

    val intake = Intake(hardware.intakeMotor, hardware.loadingSensor)
    val turret = Turret(hardware.turretMotor, hardware.turretLimitSwitch)
    val pitch = Pitch(hardware.pitchMotor, hardware.pitchLimitSwitch)
    val slides = Slides(hardware.slideMotor)

    val indexer = Indexer(hardware.indexerServo)
    val outtake = Outtake(hardware.outtakeServo)

    var hubState = HubState.ALLIANCE_HIGH

    fun incrementState() {
        hubState = HubState.values()[min(hubState.ordinal+1, HubState.values().size-1)]
    }

    fun decrementState() {
        hubState = HubState.values()[max(hubState.ordinal-1, 0)]
    }
}