package robotuprising.ftc2021.subsystems.osiris.hardware

import robotuprising.ftc2021.util.Constants
import robotuprising.ftc2021.hardware.osiris.OsirisServo
import robotuprising.ftc2021.hardware.osiris.interfaces.Initializable
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.ftc2021.subsystems.osiris.Subsystem
import robotuprising.lib.math.*
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.Extensions.mmToIn
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

object Odometry : Subsystem(), Loopable, Initializable {
//    private val leftServo = OsirisServo("leftRetract")
//    private val rightServo = OsirisServo("rightRetract")
//    private val auxServo = OsirisServo("auxRetract")

    val TICKS_PER_INCH = 1892.3724 // (ticks per rev) / (2 * pi * r)
    @JvmField var turnScalar: Double = 14.9691931
    @JvmField var auxTracker: Double = 3.85

    var startPose = Pose(AngleUnit.RAD)
        set(value) {
            currentPosition = value.copy
            field = value
        }

    private var startL: Double = 0.0
    private var startR: Double = 0.0
    private var startA: Double = 0.0

    var currentPosition: Pose = Pose(AngleUnit.RAD)

    private var lastLeftEncoder = 0.0
    private var lastRightEncoder = 0.0
    private var lastAuxEncoder = 0.0

    private var accumHeading = 0.0

    private fun updatePose(currLeftEncoder: Double, currRightEncoder: Double, currAuxEncoder: Double) {
        val actualCurrLeft = -(currLeftEncoder - startL)
        val actualCurrRight = -(currRightEncoder - startR)
        val actualCurrAux = (currAuxEncoder - startA)

        val lWheelDelta = (actualCurrLeft - lastLeftEncoder) / TICKS_PER_INCH
        val rWheelDelta = (actualCurrRight - lastRightEncoder) / TICKS_PER_INCH
        val aWheelDelta = (actualCurrAux - lastAuxEncoder) / TICKS_PER_INCH

        val leftTotal = actualCurrLeft / TICKS_PER_INCH
        val rightTotal = actualCurrRight / TICKS_PER_INCH

        val lastAngle = currentPosition.h.copy
        val newAngle =  -Angle(((leftTotal - rightTotal) / turnScalar), AngleUnit.RAD) + startPose.h

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

        val incrementX = lastAngle.cos * deltaY - lastAngle.sin * deltaX
        val incrementY = lastAngle.sin * deltaY + lastAngle.cos * deltaX
        val pointIncrement = Point(incrementX, incrementY)

        currentPosition = Pose(currentPosition.p + pointIncrement, newAngle)

        lastLeftEncoder = actualCurrLeft
        lastRightEncoder = actualCurrRight
        lastAuxEncoder = actualCurrAux
    }

    override fun stop() {
        accumHeading = 0.0
        lastLeftEncoder = 0.0
        lastRightEncoder = 0.0
        lastAuxEncoder = 0.0
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard.addSpace()
        OsirisDashboard["current position"] = currentPosition
        OsirisDashboard["l"] = lastLeftEncoder
        OsirisDashboard["r"] = lastRightEncoder
        OsirisDashboard["a"] = lastAuxEncoder
    }

    override fun loop() {
        updatePose(
                BulkDataManager.masterData.getMotorCurrentPosition(1).d,
                BulkDataManager.masterData.getMotorCurrentPosition(0).d,
                BulkDataManager.masterData.getMotorCurrentPosition(2).d
        )
    }

    override fun init() {
        startL = BulkDataManager.masterData.getMotorCurrentPosition(1).d
        startR = BulkDataManager.masterData.getMotorCurrentPosition(0).d
        startA = BulkDataManager.masterData.getMotorCurrentPosition(2).d
        startPose = Pose(AngleUnit.RAD)
    }
}