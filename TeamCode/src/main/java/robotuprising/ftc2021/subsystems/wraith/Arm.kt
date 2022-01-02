package robotuprising.ftc2021.subsystems.wraith

import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.wraith.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Arm : ServoSubsystem(
        ServoSubsystemConfig("arm", Constants.armHomePosition, Constants.armAllianceDepositPosition))