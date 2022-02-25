package robotuprising.koawalib.subsystem.odometry

import robotuprising.koawalib.math.Pose

interface Localized {
    val position: Pose
    val velocity: Pose

    fun localize()
}