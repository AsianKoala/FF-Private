package robotuprising.koawalib.subsystem.drive

import robotuprising.koawalib.math.Pose

data class TimePose(val pose: Pose, val timestamp: Long = System.currentTimeMillis())
