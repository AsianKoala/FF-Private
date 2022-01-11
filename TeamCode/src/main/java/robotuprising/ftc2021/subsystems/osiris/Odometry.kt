package robotuprising.ftc2021.subsystems.osiris

import robotuprising.ftc2021.util.Constants
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Pose
import robotuprising.lib.math.TimePose
import robotuprising.lib.opmode.OsirisDashboard
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

// todo figure out how to zero
object Odometry : Subsystem() {
    private val leftServo = OsirisServo("leftRetract")
    private val rightServo = OsirisServo("rightRetract")
    private val auxServo = OsirisServo("auxRetract")

    const val TICKS_PER_INCH = 1103.8839
    @JvmField var turnScalar: Double = 14.9691931
    @JvmField var auxTracker: Double = 3.85

    private var startPose = Pose.DEFAULT_RAW.copy

    private var startL: Double = 0.0
    private var startR: Double = 0.0
    private var startA: Double = 0.0

    var currentPosition: Pose = Pose.DEFAULT_RAW
    var currentVelocity: Pose = Pose.DEFAULT_ANGLE

    private var allRawDeltas: ArrayList<TimePose> = ArrayList()

    private var lastLeftEncoder = 0.0
    private var lastRightEncoder = 0.0
    private var lastAuxEncoder = 0.0

    private var accumHeading = 0.0

    fun updatePose(currLeftEncoder: Double, currRightEncoder: Double, currAuxEncoder: Double) {
        val actualCurrLeft = -(currLeftEncoder - startL)
        val actualCurrRight = (currRightEncoder - startR)
        val actualCurrAux = (currAuxEncoder - startA)

        val lWheelDelta = (actualCurrLeft - lastLeftEncoder) / TICKS_PER_INCH
        val rWheelDelta = (actualCurrRight - lastRightEncoder) / TICKS_PER_INCH
        val aWheelDelta = (actualCurrAux - lastAuxEncoder) / TICKS_PER_INCH

        val leftTotal = actualCurrLeft / TICKS_PER_INCH
        val rightTotal = actualCurrRight / TICKS_PER_INCH

        val lastAngle = currentPosition.h.copy
        currentPosition.h = -Angle(((leftTotal - rightTotal) / turnScalar), AngleUnit.RAD) + startPose.h

        val angleIncrement = (lWheelDelta - rWheelDelta) / turnScalar
        val auxPrediction = angleIncrement * auxTracker
        val rX = aWheelDelta - auxPrediction

        accumHeading += angleIncrement

        var deltaY = (lWheelDelta - rWheelDelta) / 2.0
        var deltaX = rX

        if (angleIncrement.absoluteValue > 0) {
            val radiusOfMovement = (lWheelDelta + rWheelDelta) / (2 * angleIncrement)
            val radiusOfStrafe = rX / angleIncrement

            deltaX = radiusOfMovement * (1 - cos(angleIncrement)) + (radiusOfStrafe * sin(angleIncrement))
            deltaY = (radiusOfMovement * sin(angleIncrement)) + (radiusOfStrafe * (1 - cos(angleIncrement)))
        }

        currentPosition.p.x += lastAngle.cos * deltaY - lastAngle.sin * deltaX
        currentPosition.p.y += lastAngle.sin * deltaY + lastAngle.cos * deltaX

        lastLeftEncoder = actualCurrLeft
        lastRightEncoder = actualCurrRight
        lastAuxEncoder = actualCurrAux
    }

    fun extend() {
        leftServo.position = Constants.leftOdoExtend
        rightServo.position = Constants.rightOdoExtend
        auxServo.position = Constants.auxOdoExtend
    }

    fun retract() {
        leftServo.position = Constants.leftOdoRetract
        rightServo.position = Constants.rightOdoRetract
        auxServo.position = Constants.auxOdoRetract
    }

    fun initOdo(startPosition: Pose, startLeft: Double, startRight: Double, startAux: Double) {
        startPose = startPosition
        currentPosition = startPose.copy
        startL = startLeft
        startR = startRight
        startA = startAux
    }

    override fun reset() {
        extend()
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard["current position"] = currentPosition
    }
}