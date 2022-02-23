package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.Intake
import robotuprising.ftc2021.v3.subsystems.Kei
import robotuprising.koawalib.subsystem.drive.OdoConfig

class Rin(hardware: Hardware) {
    val intake = Intake(hardware.intakeMotor, hardware.loadingSensor)

    val kei = Kei(hardware.flMotor, hardware.blMotor, hardware.frMotor, hardware.brMotor,
            OdoConfig(1892.3724, 8.690685, 7.641969,
                    hardware.frMotor, hardware.blMotor, hardware. brMotor, -1.0, -1.0))
}