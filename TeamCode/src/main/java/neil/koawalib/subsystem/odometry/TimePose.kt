package neil.koawalib.subsystem.odometry

import neil.koawalib.math.Pose

data class TimePose(val pose: Pose, val timestamp: Long = System.currentTimeMillis())
