package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.Intake

class Robot(hardware: Hardware) {
    val intakeSubsystem = Intake(hardware.intakeMotor, hardware.loadingSensor)
}