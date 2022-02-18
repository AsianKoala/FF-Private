package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.Intake
import robotuprising.ftc2021.v3.subsystems.Kei

class Rin(hardware: Hardware) {
    val intakeSubsystem = Intake(hardware.intakeMotor, hardware.loadingSensor)
    val kei = Kei(hardware.flMotor, hardware.blMotor, hardware.frMotor, hardware.brMotor)
}