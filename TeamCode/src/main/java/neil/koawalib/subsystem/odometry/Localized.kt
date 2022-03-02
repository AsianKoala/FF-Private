package neil.koawalib.subsystem.odometry

import neil.koawalib.math.Pose

interface Localized {
    val position: Pose
    val velocity: Pose

    fun localize()
}