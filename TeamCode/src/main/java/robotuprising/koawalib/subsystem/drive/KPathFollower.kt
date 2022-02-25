package robotuprising.koawalib.subsystem.drive

class KPathFollower {
//    fun bruH() {
//        when(driveState) {
//            DriveStates.DISABLED -> {
//                powers = Pose()
//            }
//
//            DriveStates.MANUAL -> {
//                // expect manual powers being set externally
//            }
//
//            DriveStates.PATH -> {
//                if(currentPath != null) {
//                    if(currentPath!!.finished) {
//                        driveState = DriveStates.DISABLED
//                        currentPath = null
//                    } else {
//                        powers = currentPath!!.update(position, velocity)
//                    }
//                } else {
//                    throw Exception("Must have cached path!!!!")
//                }
//            }
//
//            DriveStates.TARGET_POINT -> {
//                if(targetWaypoint != null) {
//                    when(targetWaypoint) {
//                        is StopWaypoint -> {
//                            val stopWaypoint = targetWaypoint as StopWaypoint
//
//                            if(position.point.distance(stopWaypoint.point) < stopWaypoint.allowedPositionError) {
//                                driveState = DriveStates.DISABLED
//                                targetWaypoint = null
//                            }
//                        }
//
//                        else -> {
//                            if(position.point.distance(targetWaypoint!!.point) < acceptableTargetError) {
//                                driveState = DriveStates.DISABLED
//                                targetWaypoint = null
//                            }
//                        }
//                    }
//
//                    powers = MecanumPurePursuitController.goToPosition(
//                            position,
//                            velocity,
//                            targetWaypoint!!
//                    )
//                }
//            }
//        }
//    }
}