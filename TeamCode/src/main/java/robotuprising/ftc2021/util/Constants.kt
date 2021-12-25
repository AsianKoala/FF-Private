package robotuprising.ftc2021.util

import robotuprising.lib.util.Extensions.mmToIn

object Constants {

    // used in determining the three dimensional pose of the superstructure
    const val turretCenterXOffset = 0.0 // relative to robot center, inches
    const val turretCenterYOffset = 0.0 // relative to robot center, inches
    const val turretCenterZOffset = 0.0 // relative to the ground, inches

    const val slideMountAngle = 180.0 - 10.0 // degrees, relative to the robot
    const val slideTopXOffset = 0.0 // relative to turret center, inches
    const val slideTopYOffset = 0.0 // relative to turret center, inches
    const val slideTopZOffset = 0.0 // relative to the ground, inches

    const val armMountDistanceAlongSlide = 0.0 // inches
    const val armLength = 0.0 // inches
    const val armMountAngle = slideMountAngle

    const val slideStages = 5


    const val armAngleThresholdForCollision = 0.0 // relative to slides, degrees
    const val slideExtendThresholdForCollision = 0.0 // relative to the slide, inches

    const val turretAngleForCollision = 0.0



    const val loadingSensorThreshold = 25.0


    val slideSpoolRadius = 35.0.mmToIn


    const val leftOdoRetract = 1.0
    const val rightOdoRetract = 1.0
    const val auxOdoRetract = 1.0

    const val leftOdoExtend = 0.0
    const val rightOdoExtend = 0.0
    const val auxOdoExtend = 0.0


}