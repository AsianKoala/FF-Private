package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubMotor
import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.lib.hardware.Status
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem

object Intake : Subsystem() {
    private lateinit var intakeMotor: ExpansionHubMotor
    private lateinit var intakePivot: ExpansionHubServo
    private lateinit var intakeSensor: ColorSensor

    private const val RESTING = 1.0 // todo
    private const val OUT = 0.0 // todo
    private val CUBE_RGB_THRESHOLD = Triple(255, 255, 255) // todo
    private val BALL_RGB_THRESHOLD = Triple(255, 255, 255) // todo

    private enum class IntakeStates {
        ON,
        OFF,
        REVERSE,
    }

    private enum class PivotStates {
        IN,
        OUT
    }

    private enum class SensorStates {
        NONE,
        CUBE,
        BALL
    }

    private var lastState = IntakeStates.OFF
    private var intakeState = IntakeStates.OFF
    private var lastPivotState = PivotStates.IN
    private var pivotState = PivotStates.IN
    private var sensorState = SensorStates.NONE

    private var max = 1.0
    private var power = 0.0

    fun turnOn() {
        intakeState = IntakeStates.ON
    }

    fun turnOff() {
        intakeState = IntakeStates.OFF
    }

    fun turnReverse() {
        intakeState = IntakeStates.REVERSE
    }

    fun customControl(ePower: Double) {
        power = ePower
    }

    fun setMax(max: Double) {
        Intake.max = max
    }

    fun rotateOut() {
        pivotState = PivotStates.OUT
    }

    fun rotateIn() {
        pivotState = PivotStates.IN
    }

    private val ColorSensor.rgb: Triple<Int, Int, Int> get() = Triple(red(), blue(), green())

    private fun Triple<Int, Int, Int>.thresholdCompare(other: Triple<Int, Int, Int>): Boolean =
            first < other.first && second < other.second && third < other.third

    private fun Triple<Int, Int, Int>.sumThreshCompare(other: Triple<Int, Int, Int>): Boolean =
            (first + second + third) < (other.first + other.second + other.third)

    override fun init(hwMap: HardwareMap) {
        intakeMotor = hwMap[ExpansionHubMotor::class.java, "intake"]
        intakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        intakePivot = hwMap[ExpansionHubServo::class.java, "intakePivot"]

        intakeSensor = hwMap[ColorSensor::class.java, "intakeSensor"]
    }

    override fun update() {
        if (status == Status.EMERGENCY) {
            setHWValues(power)
        } else {
            power = when (intakeState) {
                IntakeStates.ON -> max
                IntakeStates.OFF -> 0.0
                IntakeStates.REVERSE -> -max
            }

            if (lastState != intakeState) {
                setHWValues(power)
            }
            lastState = intakeState
        }

        if (pivotState != lastPivotState) {
            lastPivotState = pivotState
            intakePivot.position = when (pivotState) {
                PivotStates.IN -> RESTING
                PivotStates.OUT -> OUT
            }
        }

        sensorState = when {
            intakeSensor.rgb.thresholdCompare(BALL_RGB_THRESHOLD) -> SensorStates.BALL
            intakeSensor.rgb.thresholdCompare(CUBE_RGB_THRESHOLD) -> SensorStates.CUBE
            else -> SensorStates.NONE
        }
    }

    override fun sendDashboardPacket() {
        AkemiDashboard.addData("max", max)
        AkemiDashboard.addData("curr intake state", intakeState)
        AkemiDashboard.addData("curr intake power", power)
        AkemiDashboard.addData("pivot state", pivotState)
        AkemiDashboard.addData("resting val", RESTING)
        AkemiDashboard.addData("out val", OUT)
    }

    override fun stop() {
        turnOff()
    }

    override var status: Status = Status.ALIVE

    private fun setHWValues(powerV: Double) {
        intakeMotor.power = powerV
    }
}

