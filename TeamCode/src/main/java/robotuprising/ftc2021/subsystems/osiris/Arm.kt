package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystem
import robotuprising.ftc2021.subsystems.osiris.motor.ServoSubsystemConfig
import robotuprising.ftc2021.util.Constants

object Arm : ServoSubsystem(
        ServoSubsystemConfig("arm", Constants.armHomePosition, Constants.armAllianceDepositPosition))