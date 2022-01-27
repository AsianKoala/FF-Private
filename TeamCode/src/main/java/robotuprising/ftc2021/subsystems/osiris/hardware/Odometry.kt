package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.util.Constants
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Pose
import robotuprising.lib.math.TimePose
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.util.Extensions.d
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

// todo figure out how to zero
object Odometry : Subsystem(), Loopable, Initializable {
    private val leftServo = OsirisServo("leftRetract")
    private val rightServo = OsirisServo("rightRetract")
    private val auxServo = OsirisServo("auxRetract")

    const val TICKS_PER_INCH = 1103.8839
    @JvmField var turnScalar: Double = 14.9691931
    @JvmField var auxTracker: Double = 3.85

    var startPose = Pose.DEFAULT_RAW.copy

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

    private fun updatePose(currLeftEncoder: Double, currRightEncoder: Double, currAuxEncoder: Double) {
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

    override fun stop() {
        extend()
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard["current position"] = currentPosition
    }

    override fun loop() {
        updatePose(
                BulkDataManager.masterData.getMotorCurrentPosition(0).d,
                BulkDataManager.masterData.getMotorCurrentPosition(1).d,
                BulkDataManager.masterData.getMotorCurrentPosition(2).d
        )
    }

    override fun init() {
        startL = BulkDataManager.masterData.getMotorCurrentPosition(0).d
        startR = BulkDataManager.masterData.getMotorCurrentPosition(1).d
        startA = BulkDataManager.masterData.getMotorCurrentPosition(2).d
    }
}