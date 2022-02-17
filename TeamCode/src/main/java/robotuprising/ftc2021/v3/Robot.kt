package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.IntakeSubsystem

class Robot(hardware: Hardware) {
    val intakeSubsystem = IntakeSubsystem(hardware.intakeMotor)
}