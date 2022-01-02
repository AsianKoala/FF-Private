package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Outtake: ServoSubsystem(
        ServoSubsystemConfig("outtake", Constants.outtakeReadyPosition, Constants.outtakeDepositPosition))