package robotuprising.ftc2021.v3

import robotuprising.ftc2021.v3.subsystems.IntakeSubsystem
import robotuprising.koawalib.structure.KRobot

class Robot(hardware: Hardware) : KRobot() {
    val intakeSubsystem = IntakeSubsystem(hardware.intakeMotor, hardware.loadingSensor)
}