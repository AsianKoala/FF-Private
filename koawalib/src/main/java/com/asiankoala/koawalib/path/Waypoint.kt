package com.asiankoala.koawalib.path

import com.asiankoala.koawalib.math.Point
import com.asiankoala.koawalib.math.radians

data class Waypoint(
    val x: Double,
    val y: Double,
    val followDistance: Double,
    val headingLockAngle: Double? = null,
    val maxMoveSpeed: Double = 1.0,
    val maxTurnSpeed: Double = 1.0,
    val deccelAngle: Double = 60.0.radians,
    val stop: Boolean = true,
    val minAllowedHeadingError: Double = 60.0.radians,
    val lowestSlowDownFromHeadingError: Double = 0.4,
    val minAllowedXError: Double = 1.0,
    val lowestSlowDownFromXError: Double = 0.4,
) {
    val point = Point(x, y)

    val copy: Waypoint
        get() = Waypoint(
            x, y, followDistance, headingLockAngle, maxMoveSpeed, maxTurnSpeed, deccelAngle,
            stop, minAllowedHeadingError, lowestSlowDownFromHeadingError, minAllowedXError, lowestSlowDownFromXError
        )
}
