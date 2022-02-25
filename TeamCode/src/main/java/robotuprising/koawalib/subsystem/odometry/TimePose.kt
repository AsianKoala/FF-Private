package robotuprising.koawalib.subsystem.odometry

import robotuprising.koawalib.math.Pose

data class TimePose(val pose: Pose, val timestamp: Long = System.currentTimeMillis())
