package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.hardware.bosch.BNO055IMU
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriLocalizerPacket
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.Subsystem
import kotlin.math.PI
import kotlin.math.cos

class NakiriLocalizer(
    private val ultrasonics: Ultrasonics,
    private val imu: BNO055IMU,
    private var nakiriLocalizerPacket: NakiriLocalizerPacket
) : Subsystem {

    private lateinit var lastWheelPositions: List<Double>
    private var lastHeading = Angle(Double.NaN, AngleUnit.RAD)

    private var nakiriRegion = Regions.MAIN
    enum class Regions {
        MAIN,
        CRATER,
        PIPES
    }

    // TODO FIX SETTING POSE
    private var _poseEstimate = Pose2d()
    private var _poseVelocity = Pose2d()

    private val internalLocalizerObj = object : Localizer {
        override var poseEstimate: Pose2d
            get() = _poseEstimate
            set(value) {
                lastWheelPositions = emptyList()
                lastHeading = Angle(Double.NaN, AngleUnit.RAD)
                _poseEstimate = value
            }

        override val poseVelocity: Pose2d?
            get() = _poseVelocity

        override fun update() {
        }
    }

    var hasLocalizedInCraterYet = false
    private fun localizeWithUltrasonics() {
        var dForward = -1
        var dHoriz = -1

        if (!ultrasonics.isReading) {
            ultrasonics.startReading()
        }

        if (ultrasonics.finishedReadInterval) {
            dForward = ultrasonics.forwardReading
            dHoriz = ultrasonics.horizontalReading
            ultrasonics.stopReading()

            _poseEstimate = if (Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
                Pose2d(
                    dForward * lastHeading.sin,
                    dHoriz * (lastHeading + Angle(PI / 2, AngleUnit.RAD)).sin,
                    lastHeading.angle
                )
            } else {
                Pose2d(
                    dForward * lastHeading.cos,
                    dHoriz * (lastHeading - Angle(PI / 2, AngleUnit.RAD)).cos,
                    lastHeading.angle
                )
            }
        }
    }

    private fun localizeWithDrive(packet: NakiriLocalizerPacket) {
        val wheelPositions = packet.wheelPositions
        val heading = packet.heading
        if (lastWheelPositions.isNotEmpty()) {
            val wheelDeltas = wheelPositions
                .zip(lastWheelPositions)
                .map { it.first - it.second }
            val robotPoseDelta = MecanumKinematics.wheelToRobotVelocities(
                wheelDeltas,
                packet.trackWidth,
                packet.wheelBase,
                packet.lateralMultiplier
            )
            val finalHeadingDelta = heading - lastHeading
            _poseEstimate = Kinematics.relativeOdometryUpdate(
                _poseEstimate,
                Pose2d(robotPoseDelta.vec(), finalHeadingDelta.angle)
            )
        }

        val wheelVelocities = packet.wheelVelocities
        val headingVelocity = packet.headingVelocity
        if (wheelVelocities != null) {
            _poseVelocity = MecanumKinematics.wheelToRobotVelocities(
                wheelVelocities,
                packet.trackWidth,
                packet.wheelBase,
                packet.lateralMultiplier
            )
            if (headingVelocity != null) {
                _poseVelocity = Pose2d(_poseVelocity.vec(), headingVelocity)
            }
        }

        lastWheelPositions = wheelPositions
        lastHeading = heading
    }

    // TODO
    private fun regionLogic() {
    }

    override fun update() {
        when (nakiriRegion) {
            Regions.MAIN -> localizeWithDrive(nakiriLocalizerPacket)
            Regions.CRATER -> {
                if (!hasLocalizedInCraterYet) {
                    localizeWithUltrasonics()
                    hasLocalizedInCraterYet = true
                } else {
                    localizeWithDrive(nakiriLocalizerPacket)
                }
            }
            Regions.PIPES -> {}
        }
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        TODO("Not yet implemented")
    }
}
