package robotuprising.koawalib.subsystem.drive

import robotuprising.koawalib.math.Pose

interface Localized {
    val position: Pose
    val velocity: Pose

    fun localize()
}