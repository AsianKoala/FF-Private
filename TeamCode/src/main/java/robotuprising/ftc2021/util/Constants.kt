package robotuprising.ftc2021.util

import robotuprising.lib.util.Extensions.mmToIn

// todo get this from cad
object Constants {

    // used in determining the three dimensional vector of the superstructure
    const val turretCenterXOffset = 0.0 // relative to robot center, inches
    const val turretCenterYOffset = 0.0 // relative to robot center, inches
    const val turretCenterZOffset = 0.0 // relative to the ground, inches

    const val slideMountAngle = 0.0 // degrees, relative to the robot
    const val slideTopXOffset = 0.0 // relative to turret center, inches
    const val slideTopYOffset = 0.0 // relative to turret center, inches

    const val armLength = 0.0 // inches
    const val armMountAngle = 0.0

    const val slideStages = 5


    const val outtakeMountAngle = 0.0


    const val loadingSensorThreshold = 0.0

    const val intakeSensorThreshold = 0.0


    val slideSpoolRadius = 35.0.mmToIn


    const val leftOdoRetract = 1.0
    const val rightOdoRetract = 1.0
    const val auxOdoRetract = 1.0

    const val leftOdoExtend = 0.0
    const val rightOdoExtend = 0.0
    const val auxOdoExtend = 0.0


    const val outtakeReadyPosition = 0.0
    const val outtakeDepositPosition = 0.0

    const val indexerOpenPosition = 0.0
    const val indexerLockedPosition = 0.0
    const val indexerIndexingPosition = 0.0

    const val armHomePosition = 0.0
    const val armAllianceDepositPosition = 0.0


    const val duckWheelDiameter = 0.0
    const val carouselDiameter = 1.0

    const val turretPostZeroValue = 0.0

    const val slidePostZeroValue = 0.0



    const val turretRedDepositAngle = 0.0
    const val turretBlueDepositAngle = 0.0
    const val slideDepositInches = 0.0


    const val TILE = 24.0
}