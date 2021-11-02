package robotuprising.ftc2021.auto.trajectorysequence

import robotuprising.ftc2021.auto.util.DashboardUtil.drawSampledPath
import robotuprising.ftc2021.auto.util.DashboardUtil.drawRobot
import robotuprising.ftc2021.auto.util.DashboardUtil.drawPoseHistory
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.util.NanoClock
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.TrajectoryMarker
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import robotuprising.ftc2021.auto.trajectorysequence.sequencesegment.SequenceSegment
import robotuprising.ftc2021.auto.trajectorysequence.sequencesegment.TrajectorySegment
import robotuprising.ftc2021.auto.trajectorysequence.sequencesegment.TurnSegment
import robotuprising.ftc2021.auto.trajectorysequence.sequencesegment.WaitSegment
import java.util.*

@Config
class TrajectorySequenceRunner(private val follower: TrajectoryFollower, headingPIDCoefficients: PIDCoefficients?) {
    private val turnController: PIDFController
    private val clock: NanoClock
    private var currentTrajectorySequence: TrajectorySequence? = null
    private var currentSegmentStartTime = 0.0
    private var currentSegmentIndex = 0
    private var lastSegmentIndex = 0
    var lastPoseError = Pose2d()
        private set
    var remainingMarkers: MutableList<TrajectoryMarker> = ArrayList()
    private val dashboard: FtcDashboard
    private val poseHistory = LinkedList<Pose2d>()
    fun followTrajectorySequenceAsync(trajectorySequence: TrajectorySequence?) {
        currentTrajectorySequence = trajectorySequence
        currentSegmentStartTime = clock.seconds()
        currentSegmentIndex = 0
        lastSegmentIndex = -1
    }

    fun update(poseEstimate: Pose2d, poseVelocity: Pose2d?): DriveSignal? {
        var targetPose: Pose2d? = null
        var driveSignal: DriveSignal? = null
        val packet = TelemetryPacket()
        val fieldOverlay = packet.fieldOverlay()
        var currentSegment: SequenceSegment? = null
        if (currentTrajectorySequence != null) {
            if (currentSegmentIndex >= currentTrajectorySequence!!.size()) {
                for ((_, callback) in remainingMarkers) {
                    callback.onMarkerReached()
                }
                remainingMarkers.clear()
                currentTrajectorySequence = null
            }
            if (currentTrajectorySequence == null) return DriveSignal()
            val now = clock.seconds()
            val isNewTransition = currentSegmentIndex != lastSegmentIndex
            currentSegment = currentTrajectorySequence!![currentSegmentIndex]
            if (isNewTransition) {
                currentSegmentStartTime = now
                lastSegmentIndex = currentSegmentIndex
                for ((_, callback) in remainingMarkers) {
                    callback.onMarkerReached()
                }
                remainingMarkers.clear()
                remainingMarkers.addAll(currentSegment.markers)
                remainingMarkers.sortWith { (t), (time) -> t.compareTo(time) } // todo
            }
            val deltaTime = now - currentSegmentStartTime
            if (currentSegment is TrajectorySegment) {
                val currentTrajectory = currentSegment.trajectory
                if (isNewTransition) follower.followTrajectory(currentTrajectory)
                if (!follower.isFollowing()) {
                    currentSegmentIndex++
                    driveSignal = DriveSignal()
                } else {
                    driveSignal = follower.update(poseEstimate, poseVelocity)
                    lastPoseError = follower.lastError
                }
                targetPose = currentTrajectory[deltaTime]
            } else if (currentSegment is TurnSegment) {
                val targetState = currentSegment.motionProfile[deltaTime]
                turnController.targetPosition = targetState.x
                val correction = turnController.update(poseEstimate.heading)
                val targetOmega = targetState.v
                val targetAlpha = targetState.a
                lastPoseError = Pose2d(0.0, 0.0, turnController.lastError)
                val startPose = currentSegment.getStartPose()
                targetPose = startPose.copy(startPose.x, startPose.y, targetState.x)
                driveSignal = DriveSignal(
                        Pose2d(0.0, 0.0, targetOmega + correction),
                        Pose2d(0.0, 0.0, targetAlpha)
                )
                if (deltaTime >= currentSegment.getDuration()) {
                    currentSegmentIndex++
                    driveSignal = DriveSignal()
                }
            } else if (currentSegment is WaitSegment) {
                lastPoseError = Pose2d()
                targetPose = currentSegment.getStartPose()
                driveSignal = DriveSignal()
                if (deltaTime >= currentSegment.getDuration()) {
                    currentSegmentIndex++
                }
            }
            while (remainingMarkers.size > 0 && deltaTime > remainingMarkers[0].time) {
                remainingMarkers[0].callback.onMarkerReached()
                remainingMarkers.removeAt(0)
            }
        }
        poseHistory.add(poseEstimate)
        if (POSE_HISTORY_LIMIT > -1 && poseHistory.size > POSE_HISTORY_LIMIT) {
            poseHistory.removeFirst()
        }
        packet.put("x", poseEstimate.x)
        packet.put("y", poseEstimate.y)
        packet.put("heading (deg)", Math.toDegrees(poseEstimate.heading))
        packet.put("xError", lastPoseError.x)
        packet.put("yError", lastPoseError.y)
        packet.put("headingError (deg)", Math.toDegrees(lastPoseError.heading))
        draw(fieldOverlay, currentTrajectorySequence, currentSegment, targetPose, poseEstimate)
        dashboard.sendTelemetryPacket(packet)
        return driveSignal
    }

    private fun draw(
            fieldOverlay: Canvas,
            sequence: TrajectorySequence?, currentSegment: SequenceSegment?,
            targetPose: Pose2d?, poseEstimate: Pose2d
    ) {
        if (sequence != null) {
            for (i in 0 until sequence.size()) {
                val segment = sequence[i]
                if (segment is TrajectorySegment) {
                    fieldOverlay.setStrokeWidth(1)
                    fieldOverlay.setStroke(COLOR_INACTIVE_TRAJECTORY)
                    drawSampledPath(fieldOverlay, segment.trajectory.path)
                } else if (segment is TurnSegment) {
                    val (x, y) = segment.getStartPose()
                    fieldOverlay.setFill(COLOR_INACTIVE_TURN)
                    fieldOverlay.fillCircle(x, y, 2.0)
                } else if (segment is WaitSegment) {
                    val (x, y) = segment.getStartPose()
                    fieldOverlay.setStrokeWidth(1)
                    fieldOverlay.setStroke(COLOR_INACTIVE_WAIT)
                    fieldOverlay.strokeCircle(x, y, 3.0)
                }
            }
        }
        if (currentSegment != null) {
            if (currentSegment is TrajectorySegment) {
                val currentTrajectory = currentSegment.trajectory
                fieldOverlay.setStrokeWidth(1)
                fieldOverlay.setStroke(COLOR_ACTIVE_TRAJECTORY)
                drawSampledPath(fieldOverlay, currentTrajectory.path)
            } else if (currentSegment is TurnSegment) {
                val (x, y) = currentSegment.getStartPose()
                fieldOverlay.setFill(COLOR_ACTIVE_TURN)
                fieldOverlay.fillCircle(x, y, 3.0)
            } else if (currentSegment is WaitSegment) {
                val (x, y) = currentSegment.getStartPose()
                fieldOverlay.setStrokeWidth(1)
                fieldOverlay.setStroke(COLOR_ACTIVE_WAIT)
                fieldOverlay.strokeCircle(x, y, 3.0)
            }
        }
        if (targetPose != null) {
            fieldOverlay.setStrokeWidth(1)
            fieldOverlay.setStroke("#4CAF50")
            drawRobot(fieldOverlay, targetPose)
        }
        fieldOverlay.setStroke("#3F51B5")
        drawPoseHistory(fieldOverlay, poseHistory)
        fieldOverlay.setStroke("#3F51B5")
        drawRobot(fieldOverlay, poseEstimate)
    }

    val isBusy: Boolean
        get() = currentTrajectorySequence != null

    companion object {
        var COLOR_INACTIVE_TRAJECTORY = "#4caf507a"
        var COLOR_INACTIVE_TURN = "#7c4dff7a"
        var COLOR_INACTIVE_WAIT = "#dd2c007a"
        var COLOR_ACTIVE_TRAJECTORY = "#4CAF50"
        var COLOR_ACTIVE_TURN = "#7c4dff"
        var COLOR_ACTIVE_WAIT = "#dd2c00"
        var POSE_HISTORY_LIMIT = 100
    }

    init {
        turnController = PIDFController(headingPIDCoefficients!!)
        turnController.setInputBounds(0.0, 2 * Math.PI)
        clock = NanoClock.system()
        dashboard = FtcDashboard.getInstance()
        dashboard.telemetryTransmissionInterval = 25
    }
}