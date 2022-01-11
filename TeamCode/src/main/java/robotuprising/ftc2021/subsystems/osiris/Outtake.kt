package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Outtake: ServoSubsystem(
        ServoSubsystemConfig("outtake", Constants.outtakeReadyPosition, Constants.outtakeDepositPosition))