package asiankoala.ftc2021

import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.math.degrees
import com.asiankoala.koawalib.math.wrap
import com.asiankoala.koawalib.subsystem.odometry.Encoder
import com.asiankoala.koawalib.subsystem.odometry.Odometry
import com.asiankoala.koawalib.util.Logger

class BetterThreeWheelOdometry(
        private val leftEncoder: Encoder,
        private val rightEncoder: Encoder,
        private val auxEncoder: Encoder,
        private val TRACK_WIDTH: Double,
        private val PERP_TRACKER: Double
) : Odometry() {
    private var encoders = listOf(leftEncoder, rightEncoder, auxEncoder)
    private var accumulatedHeading = 0.0
    private var accumulatedAuxPrediction = 0.0
    private var lastLeftEncoder = 0.0
    private var lastRightEncoder = 0.0
    private var lastAuxEncoder = 0.0

    private var leftOffset = 0.0
    private var rightOffset = 0.0
    private var auxOffset = 0.0

    override fun updateTelemetry() {
//        Logger.addTelemetryData("start pose", startPose)
//        Logger.addTelemetryData("curr pose", position)
//        Logger.addTelemetryData("left encoder", leftEncoder.position)
//        Logger.addTelemetryData("right encoder", rightEncoder.position)
//        Logger.addTelemetryData("aux encoder", auxEncoder.position)
//        Logger.addTelemetryData("accumulated heading", accumulatedHeading.degrees)
    }

    var shouldRead = false

    override fun periodic() {
        encoders.forEach(Encoder::update)

        if(shouldRead) {
            val left = leftEncoder.position - leftOffset
            val right = rightEncoder.position - rightOffset
            val aux = auxEncoder.position - auxOffset

            val leftDelta = left - lastLeftEncoder
            val rightDelta = right - lastRightEncoder
            val auxDelta = aux - lastAuxEncoder

            val newAngle = (((left - right) / TRACK_WIDTH) + startPose.heading).wrap

            val headingDelta = (leftDelta - rightDelta) / TRACK_WIDTH
            val auxPredicted = headingDelta * PERP_TRACKER
            val auxReal = auxDelta - auxPredicted

            accumulatedHeading += headingDelta
            accumulatedAuxPrediction += auxPredicted

            val deltaY = (leftDelta - rightDelta) / 2.0
            val pointIncrement = updatePoseWithDeltas(_position, leftDelta, rightDelta, auxReal, deltaY, headingDelta)
            _position = Pose(_position.point + pointIncrement, newAngle)

            Logger.addTelemetryData("aux difference", aux - accumulatedAuxPrediction)

            lastLeftEncoder = left
            lastRightEncoder = right
            lastAuxEncoder = aux
        } else {
            leftOffset = leftEncoder.position
            rightOffset = rightEncoder.position
            auxOffset = auxEncoder.position
            lastLeftEncoder = leftEncoder.position
            lastRightEncoder = rightEncoder.position
            lastAuxEncoder = auxEncoder.position
        }
    }
}
