package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.ftc2021.util.*
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem
import kotlin.math.absoluteValue

class Intake : Subsystem {
    private val intakeMotor = NakiriMotorFactory.name("intake").slave.brake.openLoopControl.create
    private val intakePivotLeft = NakiriServo("intakeLeftPivot")
    private val intakePivotRight = NakiriServo("intakeRightPivot")

    private val intakeSensor = BulkDataManager.hwMap[RevColorSensorV3::class.java, "intakeSensor"]

    private enum class IntakeStates(val power: Double) {
        ON(Globals.INTAKE_IN_POWER),
        OFF(Globals.INTAKE_NO_POWER),
        REVERSE(Globals.INTAKE_TRANSFER_POWER),
    }

    private enum class PivotStates(val leftPos: Double, val rightPos: Double) {
        IN(Globals.INTAKE_PIVOT_LEFT_IN, Globals.INTAKE_PIVOT_RIGHT_IN),
        OUT(Globals.INTAKE_PIVOT_LEFT_OUT, Globals.INTAKE_PIVOT_RIGHT_OUT)
    }

    private enum class SensorStates {
        NONE,
        MINERAL_IN
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

    fun isMineralIn(): Boolean {
        return sensorState == SensorStates.MINERAL_IN
    }

    override fun update() {
        intakeMotor.power = intakeState.power
        intakePivotLeft.position = pivotState.leftPos
        intakePivotRight.position = pivotState.rightPos

        sensorState = when {
            intakeSensor.getDistance(DistanceUnit.MM) < 28.0 -> SensorStates.MINERAL_IN
            else -> SensorStates.NONE
        }

        Globals.telemetry.addData("intake sensor state", sensorState)
        Globals.telemetry.addData("intake sensor distance", intakeSensor.getDistance(DistanceUnit.MM))
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
