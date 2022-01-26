package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.hardware.osiris.OsirisMotor
import robotuprising.lib.control.auto.path.PurePursuitController
import robotuprising.lib.control.auto.path.PurePursuitPath
import robotuprising.lib.control.auto.waypoints.StopWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.*
import kotlin.math.absoluteValue

object Ghost : Subsystem(), Loopable {
    private val fl = OsirisMotor("fl", true).brake.openLoopControl.reverse
    private val bl = OsirisMotor("bl", true).brake.openLoopControl.reverse
    private val fr = OsirisMotor("fr", true).brake.openLoopControl
    private val br = OsirisMotor("br", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

    private var _powers = Pose(Point(), Angle(0.0, AngleUnit.RAD))

    var driveState = DriveStates.DISABLED
    enum class DriveStates {
        DISABLED,
        MANUAL,
        PATH,
        TARGET_POINT
    }

    var manualPowers = Pose(Point(), Angle.EAST)
        set(value) {
            if(driveState == DriveStates.MANUAL) {
                _powers = value
            }
            field = value
        }

    var pathPowers = Pose(Point(), Angle.EAST)
        set(value) {
            if(driveState == DriveStates.PATH) {
                _powers = value
            }
            field = value
        }

    var targetPointPowers = Pose(Point(), Angle.EAST)
        set(value) {
            if(driveState == DriveStates.TARGET_POINT) {
                _powers = value
            }
            field = value
        }

    var currentPath: PurePursuitPath? = null
    var targetWaypoint: Waypoint? = null

    var acceptableTargetError = 2.0
    var acceptableStopError = 0.5

    override fun reset() {
        _powers = Pose.DEFAULT_RAW
    }

    override fun updateDashboard(debugging: Boolean) {

    }

    override fun loop() {
        when(driveState) {
            DriveStates.DISABLED -> {
                _powers.p.x = 0.0
                _powers.p.y = 0.0
                _powers.h = Angle.EAST
            }

            DriveStates.MANUAL -> {
                // expect that manual powers are being externally set
            }

            DriveStates.PATH -> {
                if(currentPath != null) {
                    if(currentPath!!.isFinished) {
                        driveState = DriveStates.DISABLED
                    } else {
                        pathPowers = currentPath!!.update(Odometry.currentPosition)
                    }
                } else {
                    throw Exception("Must have cached path to follow!!!!")
                }
            }

            DriveStates.TARGET_POINT -> {
                if(targetWaypoint != null) {
                    val currPose = Odometry.currentPosition
                    when(targetWaypoint) {
                        is StopWaypoint -> {
                            val stopWaypoint = targetWaypoint as StopWaypoint

                            if(currPose.p.distance(stopWaypoint.p) < acceptableStopError
                                    && MathUtil.angleThresh(currPose.h, stopWaypoint.h, stopWaypoint.dh)) {
                                driveState = DriveStates.DISABLED
                            }
                        }

                        else -> {
                            if(currPose.p.distance(targetWaypoint!!.p) < acceptableTargetError) {
                                driveState = DriveStates.DISABLED
                            }
                        }
                    }

                    if(driveState != DriveStates.DISABLED) {
                        targetPointPowers = PurePursuitController.curve(currPose, targetWaypoint!!)
                    }
                } else {
                    throw Exception("must have cached point to target!!!!")
                }
            }
        }

        val fl = _powers.y + _powers.x + _powers.h.angle
        val bl = _powers.y - _powers.x + _powers.h.angle
        val fr = _powers.y - _powers.x - _powers.h.angle
        val br = _powers.y + _powers.x - _powers.h.angle

        val wheels = listOf(fl, bl, fr, br)
        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!
        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

}