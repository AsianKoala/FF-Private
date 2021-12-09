package robotuprising.ftc2021.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMUImpl
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import robotuprising.ftc2021.opmodes.testing.Ultrasonics
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.lib.hardware.AxesSigns
import robotuprising.lib.hardware.BNO055IMUUtil.remapAxes
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.opmode.OpModeType
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d
import robotuprising.lib.util.Extensions.pose
import kotlin.math.absoluteValue

class Ayame: Subsystem {

    // hardware
    private val fl = NakiriMotor("FL", true).brake.openLoopControl.reverse
    private val bl = NakiriMotor("BL", true).brake.openLoopControl.reverse
    private val fr = NakiriMotor("FR", true).brake.openLoopControl
    private val br = NakiriMotor("BR", true).brake.openLoopControl
    private val motors = listOf(fl, bl, fr, br)

//    private val ultrasonics = Ultrasonics()


    // location
    var pose: Pose = Pose.DEFAULT_ANGLE
        private set

    private val imu = BulkDataManager.hwMap[BNO055IMUImpl::class.java, "imu"]
    private val headingOffset: Double
    private val yawOffset: Double
    private val pitchOffset: Double

    private val angularOrientation get() = imu.angularOrientation
    private val heading: Angle get() = Angle(angularOrientation.firstAngle - headingOffset, AngleUnit.RAD).wrap()
    private val yaw: Angle get() = Angle(angularOrientation.secondAngle - yawOffset, AngleUnit.RAD).wrap()
    private val pitch: Angle get() = Angle(angularOrientation.thirdAngle - pitchOffset, AngleUnit.RAD).wrap()

    // powers
    private var wheels: List<Double> = mutableListOf(0.0, 0.0, 0.0, 0.0)

    // localizer
    private var _poseEstimate = Pose2d()
    private var poseVelocity = Pose2d()
    private var lastWheelPositions = emptyList<Double>()
    private var lastExtHeading = Double.NaN

    var locationState = LocationStates.FIELD
    private var targetLocation = LocationStates.CRATER
    enum class LocationStates {
        CRATER,
        PIPES,
        FIELD,
        NONE,
    }

    private var startCounter = 0
    private val pipeX = 48.0 // todo


    fun setVectorPower(powers: Pose) {
        wheels = mutableListOf(
            powers.y + powers.x + powers.h.angle,
            powers.y - powers.x + powers.h.angle,
            powers.y - powers.x - powers.h.angle,
            powers.y + powers.x - powers.h.angle
        )
    }

    private fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        wheels = listOf(frontLeft, rearLeft, frontRight, rearRight)
    }

    private fun getWheelPositions(): List<Double> {
        return motors.map { it.position.d }
    }

    private fun getWheelVelocities(): List<Double> {
        return motors.map { it.velocity.d }
    }
    private fun getExternalHeadingVelocity(): Double {
        return imu.angularVelocity.xRotationRate.d
    }

    private fun updatePose() {
//        if(locationState != LocationStates.PIPES) {
//            val wheelPositions = getWheelPositions()
//            val extHeading = pose.h.angle
//            if (lastWheelPositions.isNotEmpty()) {
//                val wheelDeltas = wheelPositions
//                        .zip(lastWheelPositions)
//                        .map { it.first - it.second }
//                val robotPoseDelta = MecanumKinematics.wheelToRobotVelocities(
//                        wheelDeltas,
//                        15.6,
//                        15.6,
//                        1.0
//                )
//
//                val finalHeadingDelta = Angle(extHeading - lastExtHeading, AngleUnit.RAD).wrap().abs
//                _poseEstimate = Kinematics.relativeOdometryUpdate(
//                        _poseEstimate,
//                        Pose2d(robotPoseDelta.vec(), finalHeadingDelta)
//                )
//            }
//
//            val wheelVelocities = getWheelVelocities()
//            val extHeadingVel = getExternalHeadingVelocity()
//
//            poseVelocity = MecanumKinematics.wheelToRobotVelocities(
//                    wheelVelocities,
//                    15.6,
//                    15.6,
//                    1.0
//            )
//
//            poseVelocity = Pose2d(poseVelocity.vec(), extHeadingVel)
//
//            lastWheelPositions = wheelPositions
//            lastExtHeading = extHeading
//
//
//            pose = _poseEstimate.pose
//            NakiriDashboard.addLine("default localization")
//        } else {
//            NakiriDashboard.addLine("localizing with ultrasonics")
//
//            if(ultrasonics.counter > startCounter) {
//                val lastUltrasonicHoriz = ultrasonics.forwardRangeReading
//                val lastUltrasonicFwd = ultrasonics.horizRangeReading
//
//                val adjustedUltrasonicHoriz = lastUltrasonicHoriz * yaw.cos
//                val adjustedUltrasonicFwd = lastUltrasonicFwd * pitch.cos
//
//                NakiriDashboard["adjusted ultrasonic horiz"] = adjustedUltrasonicHoriz
//                NakiriDashboard["ajdusted ultrasonic fwd"] = adjustedUltrasonicFwd
//
//                val forwardWall = 72.0
//                val downWall = -72.0
//                val upWall = 72.0
//
//                val deltaX = forwardWall - adjustedUltrasonicFwd
//                val deltaY: Double
//
//                val xEstimate = deltaX * heading.cos // re
//                val yEstimate = if(Globals.ALLIANCE_SIDE == AllianceSide.RED) {
//                    deltaY = downWall + adjustedUltrasonicHoriz
//                    deltaY * heading.sin
//                } else {
//                    deltaY = upWall - adjustedUltrasonicHoriz
//                    deltaY * heading.cos
//                }
//
//                val ultrasonicPose = Pose(Point(xEstimate, yEstimate), heading)
//
//                val crossed = (targetLocation == LocationStates.CRATER && xEstimate > pipeX) || (targetLocation == LocationStates.FIELD && xEstimate < pipeX)
//                if(crossed) {
//                    pose = ultrasonicPose
//                    ultrasonics.stopReading()
//
//                    locationState = if(targetLocation == LocationStates.CRATER) {
//                        LocationStates.CRATER
//                    } else {
//                        LocationStates.FIELD
//                    }
//
//                    targetLocation = LocationStates.NONE
//                }
//            }
//        }
    }

    fun startGoingOverPipes() {
//        targetLocation = if(locationState == LocationStates.FIELD) {
//            LocationStates.CRATER
//        } else {
//            LocationStates.FIELD
//        }
//
//        locationState = LocationStates.PIPES
//
//        ultrasonics.startReading()
//
//        startCounter = ultrasonics.counter
    }

    // override methods
    // subsystem methods
    override fun update() {
//        if(Globals.OPMODE_TYPE == OpModeType.AUTO) {
//            updatePose()
//        }

//        ultrasonics.update()

        val absMax = wheels.map { it.absoluteValue }.maxOrNull()!!
        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheels[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheels[i] }
        }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
//        ultrasonics.sendDashboardPacket(debugging)
        if (debugging) {
            NakiriDashboard.setHeader("ayame")
            NakiriDashboard["wheel powers"] = wheels
            motors.forEach { it.sendDataToDashboard() }
        }
    }

    override fun reset() {
        setVectorPower(Pose.DEFAULT_RAW)
    }

    init {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.loggingEnabled = false
        imu.initialize(parameters)
        remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN)

        val orientation = imu.angularOrientation
        headingOffset = orientation.firstAngle.d
        yawOffset = orientation.secondAngle.d
        pitchOffset = orientation.thirdAngle.d
    }
}
