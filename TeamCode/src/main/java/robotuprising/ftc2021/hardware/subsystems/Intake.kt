package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Intake : Subsystem() {

    private val intakeMotor = NakiriMotor("intakeMotor", false).brake.openLoopControl
    private val intakePivotLeft = NakiriServo("intakePivotLeft")
    private val intakePivotRight = NakiriServo("intakePivotRight")
//    private lateinit var intakeSensor: ColorSensor

    private enum class IntakeStates(val power: Double) {
        ON(1.0),
        OFF(0.0),
        REVERSE(-1.0),
    }

    private enum class PivotStates(val leftPos: Double, val rightPos: Double) {
        IN(0.7, 0.3),
        OUT(0.2, 0.8)
    }


    private var intakeState = IntakeStates.OFF
    private var pivotState = PivotStates.IN


//    private enum class SensorStates {
//        NONE,
//        CUBE,
//        BALL
//    }
//    private var sensorState = SensorStates.NONE

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

//    private var usingSimpleThreshCompare = true
//
//    private val ColorSensor.rgb: Triple<Int, Int, Int> get() = Triple(red(), blue(), green())
//
//    private fun Triple<Int, Int, Int>.simpleThreshCompare(other: Triple<Int, Int, Int>): Boolean =
//        first < other.first && second < other.second && third < other.third
//
//    private fun Triple<Int, Int, Int>.sumThreshCompare(other: Triple<Int, Int, Int>): Boolean =
//        (first + second + third) < (other.first + other.second + other.third)
//
//    private fun sensorCompare(other: Triple<Int, Int, Int>): Boolean {
//        return if (usingSimpleThreshCompare) {
//            intakeSensor.rgb.simpleThreshCompare(other)
//        } else {
//            intakeSensor.rgb.sumThreshCompare(other)
//        }
//    }
//
//    private enum class IntakeBehaviorStates {
//        INTAKING,
//        ROTATING_BACKWARDS,
//        TRANSFERRING,
//        DONE,
//    }
//
//    private val intakeStateMachine: StateMachine<IntakeBehaviorStates> = StateMachineBuilder<IntakeBehaviorStates>()
//        .state(IntakeBehaviorStates.INTAKING)
//        .onEnter { turnOn(); rotateOut() }
//        .transition { sensorState != SensorStates.NONE }
//        .state(IntakeBehaviorStates.ROTATING_BACKWARDS)
//        .onEnter { rotateIn() }
//        .transitionTimed(1.0)
//        .state(IntakeBehaviorStates.TRANSFERRING)
//        .onEnter { turnReverse() }
//        .transitionTimed(0.5)
//        .state(IntakeBehaviorStates.DONE)
//        .onEnter { turnOff() }
//        .transition { true }
//        .build()
//
//    fun runIntakeStateMachine() {
//        intakeStateMachine.smartRun()
//    }

//    override fun init(hwMap: HardwareMap) {
//        intakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
//        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
//
//        intakeSensor = hwMap[ColorSensor::class.java, "intakeSensor"]
//    }

    override fun update() {
        intakeMotor.power = intakeState.power
        intakePivotLeft.position = pivotState.leftPos
        intakePivotRight.position = pivotState.rightPos
//
//        sensorState = when {
//            sensorCompare(BALL_RGB_THRESHOLD) -> SensorStates.BALL
//            sensorCompare(CUBE_RGB_THRESHOLD) -> SensorStates.CUBE
//            else -> SensorStates.NONE
//        }
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "intake"
        NakiriDashboard["state"] = intakeState
        NakiriDashboard["pivot state"] = pivotState
        NakiriDashboard["left pos"] = pivotState.leftPos
        NakiriDashboard["right pos"] = pivotState.rightPos
//        AkemiDashboard["color sensor"] = intakeSensor.rgb
    }

    override fun stop() {
        turnOff()
    }
}
