package robotuprising.koawalib.subsystem.drive

import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.path.MecanumPurePursuitController
import robotuprising.koawalib.path.PurePursuitPath
import robotuprising.koawalib.path.waypoints.StopWaypoint
import robotuprising.koawalib.path.waypoints.Waypoint
import robotuprising.koawalib.subsystem.Subsystem
import java.lang.Exception
import kotlin.math.absoluteValue

open class KMecanumDrive(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor
) : Subsystem {
    private val motors = listOf(fl, bl, fr, br)

    private lateinit var position: Pose
    private lateinit var velocity: Pose

    var powers = Pose()

    var currentPath: PurePursuitPath? = null
    var targetWaypoint: Waypoint? = null
    var acceptableTargetError = 2.0
    var disabled = false

    var driveState = DriveStates.DISABLED

    fun stop() {
        driveState = DriveStates.DISABLED
        powers = Pose()
        motors.forEach { it.power = 0.0 }
        currentPath = null
        targetWaypoint = null
        disabled = true
    }

    fun updatePosVel(pos: Pose, vel: Pose) {
        position = pos
        velocity = vel
    }

    override fun periodic() {
        when(driveState) {
            DriveStates.DISABLED -> {
                powers = Pose()
            }

            DriveStates.MANUAL -> {
                // expect manual powers being set externally
            }

            DriveStates.PATH -> {
                if(currentPath != null) {
                    if(currentPath!!.finished) {
                        driveState = DriveStates.DISABLED
                    } else {
                        powers = currentPath!!.update(position, velocity)
                    }
                } else {
                    throw Exception("Must have cached path!!!!")
                }
            }

            DriveStates.TARGET_POINT -> {
                if(targetWaypoint != null) {
                    when(targetWaypoint) {
                        is StopWaypoint -> {
                            val stopWaypoint = targetWaypoint as StopWaypoint

                            if(position.point.distance(stopWaypoint.point) < stopWaypoint.allowedPositionError) {
                                driveState = DriveStates.DISABLED
                            }
                        }

                        else -> {
                            if(position.point.distance(targetWaypoint!!.point) < acceptableTargetError) {
                                driveState = DriveStates.DISABLED
                            }
                        }
                    }

                    powers = MecanumPurePursuitController.goToPosition(
                            position,
                            velocity,
                            targetWaypoint!!
                    )
                }
            }
        }

        if(!disabled) {
            val fl = powers.y + powers.x + powers.h
            val bl = powers.y - powers.x + powers.h
            val fr = powers.y - powers.x - powers.h
            val br = powers.y + powers.x - powers.h

            val wheels= listOf(fl, bl, fr, br)
            val absMax = wheels.maxOf { it.absoluteValue }
            val scalar = if(absMax > 1.0) absMax else 1.0
            motors.forEachIndexed { i, it -> it.power = wheels[i] / scalar }
        }
    }

}