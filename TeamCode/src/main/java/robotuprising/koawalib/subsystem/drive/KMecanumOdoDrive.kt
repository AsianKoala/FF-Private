package robotuprising.koawalib.subsystem.drive

import com.acmerobotics.dashboard.config.Config
import robotuprising.koawalib.hardware.KMotor
import robotuprising.koawalib.math.MathUtil.cos
import robotuprising.koawalib.math.MathUtil.d
import robotuprising.koawalib.math.MathUtil.sin
import robotuprising.koawalib.math.MathUtil.wrap
import robotuprising.koawalib.math.Point
import robotuprising.koawalib.math.Pose
import robotuprising.koawalib.path.MecanumPurePursuitController
import robotuprising.koawalib.path.PurePursuitPath
import robotuprising.koawalib.path.waypoints.StopWaypoint
import robotuprising.koawalib.path.waypoints.Waypoint
import java.lang.Exception
import kotlin.math.absoluteValue
import kotlin.math.max

@Config
open class KMecanumOdoDrive(
        fl: KMotor,
        bl: KMotor,
        fr: KMotor,
        br: KMotor,
        @JvmField val config: OdoConfig
) : KMecanumDrive(fl, bl, fr, br), Localized {

    private var _position = Pose()
    override val position: Pose get() = _position

    override val velocity: Pose
        get() {
            if(prevRobotRelativePositions.size < 2) {
                return Pose()
            }

            val oldIndex = max(0, prevRobotRelativePositions.size- config.VELOCITY_READ_TICKS - 1)
            val old = prevRobotRelativePositions[oldIndex]
            val curr = prevRobotRelativePositions[prevRobotRelativePositions.size - 1]

            val scalar = (curr.timestamp - old.timestamp).toDouble() / 1000.0

            val dirVel = (curr.pose.point - old.pose.point) * (1 / scalar)
            val angularVel = (curr.pose.heading - old.pose.heading) * (1 / scalar)

            return Pose(dirVel, angularVel.wrap)
        }

    var startPose = Pose()
        set(value) {
            _position = value
            field = value
        }

    // position
    private var startL = 0.0
    private var startR = 0.0
    private var startA = 0.0

    private var lastLeftEncoder = 0.0
    private var lastRightEncoder = 0.0
    private var lastAuxEncoder = 0.0

    private var currLeftEncoder = { config.leftEncoder.position.d }
    private var currRightEncoder = { config.rightEncoder.position.d }
    private var currAuxEncoder = { config.auxEncoder.position.d }

    // velocity
    private val prevRobotRelativePositions = ArrayList<TimePose>()
    private var robotRelativeMovement = Pose()

    var currentPath: PurePursuitPath? = null
    var targetWaypoint: Waypoint? = null
    var acceptableTargetError = 2.0

    override fun localize() {
        val currLeft = currLeftEncoder.invoke()
        val currRight = currRightEncoder.invoke()
        val currAux = currAuxEncoder.invoke()

        val actualCurrLeft = config.LEFT_SCALAR * (currLeft - startL)
        val actualCurrRight = config.RIGHT_SCALAR * (currRight- startR)
        val actualCurrAux = config.AUX_SCALAR * (currAux - startA)

        val lWheelDelta = (actualCurrLeft - lastLeftEncoder) / config.TICKS_PER_INCH
        val rWheelDelta = (actualCurrRight - lastRightEncoder) / config.TICKS_PER_INCH
        val aWheelDelta = (actualCurrAux - lastAuxEncoder) / config.TICKS_PER_INCH

        val leftTotal = actualCurrLeft / config.TICKS_PER_INCH
        val rightTotal = actualCurrRight / config.TICKS_PER_INCH

        val lastAngle = _position.heading
        val newAngle = (((leftTotal - rightTotal) / config.TURN_SCALAR) + startPose.heading).wrap

        val angleIncrement = (lWheelDelta - rWheelDelta) / config.TURN_SCALAR
        val auxPrediction = angleIncrement * config.AUX_TRACKER
        val rX = aWheelDelta - auxPrediction

        var deltaY = (lWheelDelta - rWheelDelta) / 2.0
        var deltaX = rX

        if(angleIncrement.absoluteValue > 0) {
            val radiusOfMovement = (lWheelDelta + rWheelDelta) / (2 * angleIncrement)
            val radiusOfStrafe = rX / angleIncrement

            deltaX = (radiusOfMovement * (1 - angleIncrement.cos)) + (radiusOfStrafe * angleIncrement.sin)
            deltaY = (radiusOfMovement * angleIncrement.sin) + (radiusOfStrafe * (1 - angleIncrement.cos))
        }

        val robotDeltaRelativeMovement = Pose(deltaX, deltaY, angleIncrement)
        robotRelativeMovement += robotDeltaRelativeMovement

        prevRobotRelativePositions.add(TimePose(robotRelativeMovement))

        val incrementX = lastAngle.cos * deltaY - lastAngle.sin * deltaX
        val incrementY = lastAngle.sin * deltaY + lastAngle.cos * deltaX
        val pointIncrement = Point(incrementX, incrementY)

        _position = Pose(_position.point + pointIncrement, newAngle)

        lastLeftEncoder = actualCurrLeft
        lastRightEncoder = actualCurrRight
        lastAuxEncoder = actualCurrAux
    }

    override fun periodic() {
        localize()

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
                        currentPath = null
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
                                targetWaypoint = null
                            }
                        }

                        else -> {
                            if(position.point.distance(targetWaypoint!!.point) < acceptableTargetError) {
                                driveState = DriveStates.DISABLED
                                targetWaypoint = null
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

        processPowers()
    }
}