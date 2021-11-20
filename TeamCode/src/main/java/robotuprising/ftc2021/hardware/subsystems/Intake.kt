package robotuprising.ftc2021.hardware.subsystems

import robotuprising.ftc2021.util.Globals
import robotuprising.ftc2021.util.NakiriMotor
import robotuprising.ftc2021.util.NakiriServo
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Intake : Subsystem() {
    private val intakeMotor = NakiriMotor("intake", false).brake.openLoopControl
    private val intakePivotLeft = NakiriServo("intakeLeftPivot")
    private val intakePivotRight = NakiriServo("intakeRightPivot")

    private enum class IntakeStates(val power: Double) {
        ON(Globals.INTAKE_IN_POWER),
        OFF(Globals.INTAKE_NO_POWER),
        REVERSE(Globals.INTAKE_TRANSFER_POWER),
    }

    private enum class PivotStates(val leftPos: Double, val rightPos: Double) {
        IN(Globals.INTAKE_PIVOT_LEFT_OUT, Globals.INTAKE_PIVOT_RIGHT_OUT),
        OUT(Globals.INTAKE_PIVOT_LEFT_IN, Globals.INTAKE_PIVOT_RIGHT_IN)
    }


    private var intakeState = IntakeStates.OFF
    private var pivotState = PivotStates.IN

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
    }

    override fun stop() {
        turnOff()
    }
}
