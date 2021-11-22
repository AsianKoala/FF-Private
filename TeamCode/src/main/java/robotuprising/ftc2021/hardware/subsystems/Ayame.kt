package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMUImpl
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.NakiriMotorFactory
import robotuprising.lib.hardware.AxesSigns
import robotuprising.lib.hardware.BNO055IMUUtil.remapAxes
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.Extensions.d
import kotlin.math.PI
import kotlin.math.absoluteValue

class Ayame(
    kV: Double,
    kA: Double,
    kStatic: Double,
    trackWidth: Double
) :
    MecanumDrive(kV, kA, kStatic, trackWidth), Subsystem {
    companion object {
        private const val WHEEL_RADIUS = 1.88976
        private const val GEAR_RATIO = 1.0
        private const val TICKS_PER_REV = 537.7

        private fun ticksToInches(ticks: Int): Double {
            return WHEEL_RADIUS * 2 * PI * GEAR_RATIO * ticks / TICKS_PER_REV
        }
    }
//
//    private val fl = NakiriMotor("FL", true).brake.openLoopControl
//    private val bl = NakiriMotor("BL", true).brake.openLoopControl
//    private val fr = NakiriMotor("FR", true).brake.openLoopControl
//    private val br = NakiriMotor("BR", true).brake.openLoopControl
    private val fl = NakiriMotorFactory.name("FL").master.brake.openLoopControl.forward.create
    private val bl = NakiriMotorFactory.name("BL").master.brake.openLoopControl.forward.create
    private val fr = NakiriMotorFactory.name("FR").master.brake.openLoopControl.forward.create
    private val br = NakiriMotorFactory.name("BR").master.brake.openLoopControl.forward.create
    private val motors = listOf(fl, bl, fr, br)

    private val imu = BulkDataManager.hwMap[BNO055IMUImpl::class.java, "imu"]
    private val headingOffset: Double
    private val imuOffsetRead: Double get() = imu.angularOrientation.firstAngle - headingOffset

    private var rrControlled = false
    private var wheelPowers = mutableListOf<Double>()

    override val rawExternalHeading: Double
        get() = imuOffsetRead

    override fun getWheelPositions(): List<Double> {
        return motors.map { it.position.d }
    }

    override fun setMotorPowers(frontLeft: Double, rearLeft: Double, rearRight: Double, frontRight: Double) {
        if (rrControlled) {
            wheelPowers = mutableListOf(frontLeft, rearLeft, frontRight, rearRight)
        }
    }

    override fun update() {
        val absMax = wheelPowers.map { it.absoluteValue }.maxOrNull()!!
        if (absMax > 1.0) {
            motors.forEachIndexed { i, it -> it.power = wheelPowers[i] / absMax }
        } else {
            motors.forEachIndexed { i, it -> it.power = wheelPowers[i] / absMax }
        }
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "ayame"
        NakiriDashboard["wheel powers"] = wheelPowers
    }

    override fun stop() {
    }

    init {
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.loggingEnabled = false
        imu.initialize(parameters)
        remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN)
        headingOffset = imu.angularOrientation.firstAngle.toDouble()
    }
}
