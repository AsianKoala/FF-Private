package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.auto.pp.MecanumPurePursuitController
import robotuprising.ftc2021.auto.pp.PurePursuitPath
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.hardware.osiris.OsirisMotor
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.control.auto.waypoints.StopWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.*
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.opmode.OsirisDashboard
import kotlin.math.absoluteValue

object Ghost : Subsystem(), Loopable {
    private val fl = OsirisMotor("fl", true).brake.openLoopControl.reverse
    private val bl = OsirisMotor("bl", true).brake.openLoopControl.reverse
    private val fr = OsirisMotor("fr", true).brake.openLoopControl
    private val br = OsirisMotor("br", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

    var powers = Pose(Point(), Angle(0.0, AngleUnit.RAW))

    var driveState = DriveStates.DISABLED
    enum class DriveStates {
        DISABLED,
        MANUAL,
        PATH,
        TARGET_POINT
    }

    var currentPath: PurePursuitPath? = null
    var targetWaypoint: Waypoint? = null

    var acceptableTargetError = 2.0

    var disabled = false

    override fun stop() {
        driveState = DriveStates.DISABLED
        powers = Pose(AngleUnit.RAW)
        motors.forEach { it.power = 0.0 }
        currentPath = null
        targetWaypoint = null
        disabled = false
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard.addSpace()
        OsirisDashboard["drive state"] = driveState
        OsirisDashboard["powers"] = powers.toRawString
    }

    override fun loop() {
        val position = Odometry.currentPosition
        val velocity = Odometry.relVelocity

        when(driveState) {
            DriveStates.DISABLED -> {
                powers = Pose(AngleUnit.RAW)
            }

            DriveStates.MANUAL -> {
                // expect that manual powers are being externally set
            }

            DriveStates.PATH -> {
                if(currentPath != null) {
                    if(currentPath!!.finished) {
                        driveState = DriveStates.DISABLED
                    } else {
                        powers = currentPath!!.update()
                    }
                } else {
                    throw Exception("Must have cached path to follow!!!!")
                }
            }

            DriveStates.TARGET_POINT -> {
                if(targetWaypoint != null) {
                    when(targetWaypoint) {
                        is StopWaypoint -> {
                            val stopWaypoint = targetWaypoint as StopWaypoint

                            if(position.p.distance(stopWaypoint.p) < stopWaypoint.allowedPositionError) {
                                driveState = DriveStates.DISABLED
                            }
                        }

                        else -> {
                            if(position.p.distance(targetWaypoint!!.p) < acceptableTargetError) {
                                driveState = DriveStates.DISABLED
                            }
                        }
                    }

                    if(driveState != DriveStates.DISABLED) {
                        powers = MecanumPurePursuitController.goToPosition(
                                position,
                                velocity,
                                targetWaypoint!!
                        )
                    }
                } else {
                    throw Exception("must have cached point to target!!!!")
                }
            }
        }

        if(!disabled) {
            val fl = powers.y + powers.x + powers.h.angle
            val bl = powers.y - powers.x + powers.h.angle
            val fr = powers.y - powers.x - powers.h.angle
            val br = powers.y + powers.x - powers.h.angle

            val wheels = listOf(fl, bl, fr, br)
            val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!
            if (absMax > 1.0) {
                motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
            } else {
                motors.forEachIndexed { i, it -> it.power = wheels[i] }
            }
        }
    }

}