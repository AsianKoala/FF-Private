package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.ColorSensor
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.AkemiDashboard
import robotuprising.lib.system.Subsystem
import robotuprising.lib.system.statemachine.StateMachine
import robotuprising.lib.system.statemachine.StateMachineBuilder

class Intake : Subsystem() {
    companion object {
        private const val LEFT_IN = 1.0 // todo
        private const val LEFT_OUT = 0.0 // todo
        private const val RIGHT_IN = 0.0 // todo
        private const val RIGHT_OUT = 1.0 // todo
        private const val MAX = 1.0 // todo
        private val CUBE_RGB_THRESHOLD = Triple(255, 255, 255) // todo
        private val BALL_RGB_THRESHOLD = Triple(255, 255, 255) // todo
    }

    private val intakeMotor = NakiriMotor("intakeMotor", false)
    private val intakePivotLeft = NakiriServo("intakePivotLeft")
    private val intakePivotRight = NakiriServo("intakePivotRight")
    private lateinit var intakeSensor: ColorSensor

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

    private var intakeState = IntakeStates.OFF
    private var pivotState = PivotStates.IN

    private var sensorState = SensorStates.NONE

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

    private var usingSimpleThreshCompare = true

    private val ColorSensor.rgb: Triple<Int, Int, Int> get() = Triple(red(), blue(), green())

    private fun Triple<Int, Int, Int>.simpleThreshCompare(other: Triple<Int, Int, Int>): Boolean =
        first < other.first && second < other.second && third < other.third

    private fun Triple<Int, Int, Int>.sumThreshCompare(other: Triple<Int, Int, Int>): Boolean =
        (first + second + third) < (other.first + other.second + other.third)

    private fun sensorCompare(other: Triple<Int, Int, Int>): Boolean {
        return if(usingSimpleThreshCompare) {
            intakeSensor.rgb.simpleThreshCompare(other)
        } else {
            intakeSensor.rgb.sumThreshCompare(other)
        }
    }


    private enum class IntakeBehaviorStates {
        INTAKING,
        ROTATING_BACKWARDS,
        TRANSFERRING,
        DONE,
    }

    private val intakeStateMachine: StateMachine<IntakeBehaviorStates> = StateMachineBuilder<IntakeBehaviorStates>()
            .state(IntakeBehaviorStates.INTAKING)
            .onEnter { turnOn(); rotateOut() }
            .transition { sensorState != SensorStates.NONE }

            .state(IntakeBehaviorStates.ROTATING_BACKWARDS)
            .onEnter { rotateIn() }
            .transitionTimed(1.0)

            .state(IntakeBehaviorStates.TRANSFERRING)
            .onEnter { turnReverse() }
            .transitionTimed(0.5)

            .state(IntakeBehaviorStates.DONE)
            .onEnter { turnOff() }
            .transition { true }

            .build()

    fun runIntakeStateMachine() {
        intakeStateMachine.smartRun()
    }

//    override fun init(hwMap: HardwareMap) {
//        intakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//
//        intakeSensor = hwMap[ColorSensor::class.java, "intakeSensor"]
//    }

    override fun update() {
        intakeMotor.power = when (intakeState) {
            IntakeStates.ON -> MAX
            IntakeStates.OFF -> 0.0
            IntakeStates.REVERSE -> -MAX
        }

        intakePivotLeft.position = when (pivotState) {
            PivotStates.IN -> LEFT_IN
            PivotStates.OUT -> LEFT_OUT
        }
        intakePivotRight.position = when (pivotState) {
            PivotStates.IN -> RIGHT_IN
            PivotStates.OUT -> RIGHT_OUT
        }

        sensorState = when {
            sensorCompare(BALL_RGB_THRESHOLD) -> SensorStates.BALL
            sensorCompare(CUBE_RGB_THRESHOLD) -> SensorStates.CUBE
            else -> SensorStates.NONE
        }
    }

    override fun sendDashboardPacket() {
        AkemiDashboard["max"] = MAX
        AkemiDashboard["curr intake state"] = intakeState
        AkemiDashboard["pivot state"] = pivotState
        AkemiDashboard["resting val"] = LEFT_IN
        AkemiDashboard["out val"] = LEFT_OUT
        AkemiDashboard["color sensor"] = intakeSensor.rgb
    }

    override fun stop() {
        turnOff()
    }
}
