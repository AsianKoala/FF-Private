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

    private var lastState = IntakeStates.OFF
    private var intakeState = IntakeStates.OFF
    private enum class IntakeStates {
        ON,
        OFF,
        REVERSE,
    }

    private var max = 1.0
    private var power = 0.0


    private var pivotState = PivotStates.RESTING
    private enum class PivotStates {
        RESTING,
        OUT
    }

    private const val RESTING = 1.0 // todo
    private const val OUT = 0.0 // todo

    private var requestedPivot = false


    fun turnOn() {
        intakeState = IntakeStates.ON
    }

    fun turnOff() {
        intakeState = IntakeStates.OFF
    }

    fun reverse() {
        intakeState = IntakeStates.REVERSE
    }

    fun customControl(ePower: Double) {
        power = ePower
    }

    fun setMax(max: Double) {
        Intake.max = max
    }

    private fun changePivot(state: PivotStates) {
        pivotState = state
    }

    fun rotateOut() {
        changePivot(PivotStates.OUT)
    }

    fun rest() {
        changePivot(PivotStates.RESTING)
    }

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

        if (requestedPivot) {
            requestedPivot = false
            intakePivot.position = when (pivotState) {
                PivotStates.RESTING -> RESTING
                PivotStates.OUT -> OUT
            }
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
