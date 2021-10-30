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
    private lateinit var intakePivotLeft: ExpansionHubServo
    private lateinit var intakePivotRight: ExpansionHubServo
    private lateinit var intakeSensor: ColorSensor

    private const val LEFT_IN = 1.0 // todo
    private const val LEFT_OUT = 0.0 // todo
    private const val RIGHT_IN = 0.0 // todo
    private const val RIGHT_OUT = 1.0 // todo
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
    

    fun setMax(max: Double) {
        Intake.max = max
    }

    fun rotateOut() {
        if(pivotState == PivotStates.IN)
            pivotState = PivotStates.OUT
    }

    fun rotateIn() {
        if(pivotState == PivotStates.OUT)
            pivotState = PivotStates.IN
    }

    private val ColorSensor.rgb: Triple<Int, Int, Int> get() = Triple(red(), blue(), green())

    private fun Triple<Int, Int, Int>.thresholdCompare(other: Triple<Int, Int, Int>): Boolean =
            first < other.first && second < other.second && third < other.third

    private fun Triple<Int, Int, Int>.sumThreshCompare(other: Triple<Int, Int, Int>): Boolean =
            (first + second + third) < (other.first + other.second + other.third)

    val hasMineral get() = sensorState != SensorStates.NONE

    override fun init(hwMap: HardwareMap) {
        intakeMotor = hwMap[ExpansionHubMotor::class.java, "intake"]
        intakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        intakePivotLeft = hwMap[ExpansionHubServo::class.java, "intakePivotLeft"]
        intakePivotRight = hwMap[ExpansionHubServo::class.java, "intakePivotRight"]

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
            intakePivotLeft.position = when (pivotState) {
                PivotStates.IN -> LEFT_IN
                PivotStates.OUT -> LEFT_OUT
            }
            intakePivotRight.position = when (pivotState) {
                PivotStates.IN -> RIGHT_IN
                PivotStates.OUT -> RIGHT_OUT
            }
        }

        sensorState = when {
            intakeSensor.rgb.thresholdCompare(BALL_RGB_THRESHOLD) -> SensorStates.BALL
            intakeSensor.rgb.thresholdCompare(CUBE_RGB_THRESHOLD) -> SensorStates.CUBE
            else -> SensorStates.NONE
        }
    }

    override fun sendDashboardPacket() {
        AkemiDashboard["max"] =  max
        AkemiDashboard["curr intake state"] = intakeState
        AkemiDashboard["curr intake power"]=  power
        AkemiDashboard["pivot state"] = pivotState
        AkemiDashboard["resting val"] = LEFT_IN
        AkemiDashboard["out val"] = LEFT_OUT
    }

    override fun stop() {
        turnOff()
    }

    override var status: Status = Status.ALIVE

    private fun setHWValues(powerV: Double) {
        intakeMotor.power = powerV
    }
}

