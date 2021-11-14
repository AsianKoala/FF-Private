package robotuprising.ftc2021.control

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.localization.Localizer
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import robotuprising.lib.hardware.MB1242

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.opmode.AllianceSide


class NakiriLocalizer() : Localizer {
    private lateinit var imu: BNO055IMU
    private lateinit var ySensor: MB1242
    private lateinit var xSensor: MB1242

    // localization through ultrasonics
    private var lastUltrasonicRead = System.currentTimeMillis()
    private var requestedUltrasonicRead = false


    // default localization through drive encoders
    private lateinit var lastWheelPositions: List<Double>
    private var lastHeading = Angle(Double.NaN, AngleUnit.RAD)

    private var nakiriRegion = Regions.MAIN
    enum class Regions {
        MAIN,
        CRATER,
        SHARED,
        PIPES
    }

    private var _poseEstimate = Pose2d()
    override var poseEstimate: Pose2d
        get() = _poseEstimate
        set(value) {
            lastWheelPositions = emptyList()
            lastHeading = Angle(Double.NaN, AngleUnit.RAD)
            _poseEstimate = value
        }


    private fun localizeWithUltrasonics(allianceSide: AllianceSide) {
        if(requestedUltrasonicRead && System.currentTimeMillis() - lastUltrasonicRead > 50) {
            // TODO account for velocity during ultrasonic read
            // todo check if ultrasonics can be optimized by having
                // polling and reading in the same i2c loop
            val deltaY = ySensor.getDistance()
            val deltaX = xSensor.getDistance()

        } else {
            ySensor.poll()
            xSensor.poll()
        }
    }
    private fun localizeWithDrive(packet: NakiriLocalizerPacket) {
        val wheelPositions = packet.wheelPositions
        val heading = packet.heading
        if(lastWheelPositions.isNotEmpty()) {
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
        if(wheelVelocities != null) {
            poseVelocity = MecanumKinematics.wheelToRobotVelocities(
                    wheelVelocities,
                    packet.trackWidth,
                    packet.wheelBase,
                    packet.lateralMultiplier
            )
            if(headingVelocity != null) {
                poseVelocity = Pose2d(poseVelocity!!.vec(), headingVelocity)
            }
        }

        lastWheelPositions = wheelPositions
        lastHeading = heading
    }

    override var poseVelocity: Pose2d? = null
        private set

    override fun update() {

    }
}