package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.hardware.rev.RevColorSensorV3
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import kotlin.math.absoluteValue

class Intake : Subsystem() {
    private val intakeMotor = NakiriMotor("intake", false).brake.openLoopControl
    private val intakePivotLeft = NakiriServo("intakeLeftPivot")
    private val intakePivotRight = NakiriServo("intakeRightPivot")

    private val intakeSensor = BulkDataManager.hwMap[RevColorSensorV3::class.java, "intakeSensor"]

    private enum class IntakeStates(val power: Double) {
        ON(Globals.INTAKE_IN_POWER),
        OFF(Globals.INTAKE_NO_POWER),
        REVERSE(Globals.INTAKE_TRANSFER_POWER),
    }

    private enum class PivotStates(val leftPos: Double, val rightPos: Double) {
        IN(Globals.INTAKE_PIVOT_LEFT_OUT, Globals.INTAKE_PIVOT_RIGHT_OUT),
        OUT(Globals.INTAKE_PIVOT_LEFT_IN, Globals.INTAKE_PIVOT_RIGHT_IN)
    }

    private enum class SensorStates {
        NONE,
        BALL,
        CUBE,
        DEBUG
    }

    private var intakeState = IntakeStates.OFF
    private var pivotState = PivotStates.IN
    private var sensorState = SensorStates.NONE

    private val RevColorSensorV3.rgb get() = Triple(red(), green(), blue())

    private fun compareTriple(first: Triple<Int, Int, Int>, second: Triple<Int, Int, Int>, threshold: Int): Boolean {
        return (first.first - second.first).absoluteValue +
                (first.second - second.second).absoluteValue +
                (first.third - second.third).absoluteValue < threshold
    }


    fun turnOn() {
        intakeState = IntakeStates.ON
    }

    fun turnOff() {
        intakeState = IntakeStates.OFF
    }

    fun turnReverse() {
        intakeState = IntakeStates.REVERSE
    }

    fun rotateOut() {
        if (pivotState != PivotStates.OUT)
            pivotState = PivotStates.OUT
    }

    fun rotateIn() {
        if (pivotState != PivotStates.IN)
            pivotState = PivotStates.IN
    }


    override fun update() {
        intakeMotor.power = intakeState.power
        intakePivotLeft.position = pivotState.leftPos
        intakePivotRight.position = pivotState.rightPos

        val sensorRead = intakeSensor.rgb
        sensorState = when {
            compareTriple(sensorRead, Globals.NONE_RGB, 300) -> SensorStates.NONE
            compareTriple(sensorRead, Globals.BALL_RGB, 300) -> SensorStates.BALL
            compareTriple(sensorRead, Globals.CUBE_RGB, 300) -> SensorStates.CUBE
            else -> SensorStates.DEBUG
        }

        Globals.telemetry.addData("intake sensor state", sensorState)
        Globals.telemetry.addData("current sensor reading", sensorRead)
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "intake"
        NakiriDashboard["state"] = intakeState
        NakiriDashboard["pivot state"] = pivotState
        NakiriDashboard["left pos"] = pivotState.leftPos
        NakiriDashboard["right pos"] = pivotState.rightPos
    }

    override fun stop() {
        turnOff()
    }
}
