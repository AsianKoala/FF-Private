package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem
import robotuprising.lib.system.statemachine.StateMachineBuilder

class Nakiri : Subsystem {

    private val ayame = Ayame(0.0, 0.0, 0.0, 15.0)
    private val intake = Intake()
    private val lift = Lift()
    private val linkage = Linkage()
    private val outtake = Outtake()
    private val duckSpinner = DuckSpinner()
    private val subsystems = mutableListOf(
        ayame,
        intake,
        lift,
        linkage,
        outtake,
        duckSpinner,
    )

    private enum class IntakeSequenceStates {
        INTAKE_OUTTAKE_RESET,
        INTAKE,
        ROTATING,
        TRANSFERRING,
        OUTTAKE_TO_MEDIUM
    }

    private enum class OuttakeSequenceStates {
        LIFT_UP,
        LINKAGE_OUT,
        DEPOSIT,
        OUTTAKE_DOWN_LINKAGE_IN,
        RESET
    }

    private enum class SharedOuttakeState {
        OUTTAKE_OUT,
        OUTTAKE_IN
    }

    private val intakeSequence = StateMachineBuilder<IntakeSequenceStates>()
        .state(IntakeSequenceStates.INTAKE_OUTTAKE_RESET)
        .onEnter {
            requestIntakeRotateOut()
            requestOuttakeIn()
        }
        .transitionTimed(0.5)
        .state(IntakeSequenceStates.INTAKE)
        .onEnter { requestIntakeOn() }
        .transition { isMineralIn() }
        .state(IntakeSequenceStates.ROTATING)
        .onEnter {
            requestIntakeRotateIn()
            requestIntakeOff()
        }
        .transitionTimed(1.5)
        .state(IntakeSequenceStates.TRANSFERRING)
        .onEnter { requestIntakeReverse() }
        .transitionTimed(0.8)
        .state(IntakeSequenceStates.OUTTAKE_TO_MEDIUM)
        .onEnter {
            requestOuttakeMedium()
            requestIntakeOff()
        }
        .transition { true }
        .build()

    private val sharedOuttakeSequence = StateMachineBuilder<SharedOuttakeState>()
        .state(SharedOuttakeState.OUTTAKE_OUT)
        .onEnter { requestOuttakeOut() }
        .transitionTimed(0.75)
        .state(SharedOuttakeState.OUTTAKE_IN)
        .onEnter { requestOuttakeIn() }
        .transition { true }
        .build()

    private val closeOuttakeSequence = StateMachineBuilder<OuttakeSequenceStates>()
        .state(OuttakeSequenceStates.LIFT_UP)
        .onEnter { requestLiftHigh() }
        .transitionTimed(1.25)
        .state(OuttakeSequenceStates.LINKAGE_OUT)
        .onEnter { requestLinkageMedium() }
        .transitionTimed(0.5)
        .state(OuttakeSequenceStates.DEPOSIT)
        .onEnter { requestOuttakeOut() }
        .transitionTimed(0.75)
        .state(OuttakeSequenceStates.OUTTAKE_DOWN_LINKAGE_IN)
        .onEnter {
            requestOuttakeIn()
            requestLinkageRetract()
        }
        .transitionTimed(0.5)
        .state(OuttakeSequenceStates.RESET)
        .onEnter { requestLiftLow() }
        .transition { true }
        .build()

    fun requestAyamePowers(powers: Pose) {
        ayame.setDrivePower(powers.pose2d)
    }

    fun requestAyameStop() {
        ayame.setDrivePower(Pose2d())
    }

    fun requestIntakeOn() {
        intake.turnOn()
    }

    fun requestIntakeOff() {
        intake.turnOff()
    }

    fun requestIntakeReverse() {
        intake.turnReverse()
    }

    fun requestIntakeRotateOut() {
        intake.rotateOut()
    }

    fun requestIntakeRotateIn() {
        intake.rotateIn()
    }

    fun isMineralIn(): Boolean {
        return intake.isMineralIn()
    }

    fun requestLiftLow() {
        lift.setLevel(Lift.LiftStages.LOW)
    }

    fun requestLiftHigh() {
        lift.setLevel(Lift.LiftStages.HIGH)
    }

    fun requestLinkageRetract() {
        linkage.retract()
    }

    fun requestLinkageOut() {
        linkage.extend()
    }

    fun requestLinkageMedium() {
        linkage.extendMed()
    }

    fun requestLinkageCustom() {
        linkage.extendCustom()
    }

    fun requestOuttakeIn() {
        outtake.rotateIn()
    }

    fun requestOuttakeMedium() {
        outtake.rotateMedium()
    }

    fun requestOuttakeOut() {
        outtake.rotateOut()
    }

    fun requestSpinnerOn() {
        duckSpinner.turnOn()
    }

    fun requestSpinnerReverse() {
        duckSpinner.reverse()
    }

    fun requestSpinnerOff() {
        duckSpinner.turnOff()
    }

    fun runIntakeSequence(shouldStart: Boolean) {
        intakeSequence.smartRun(shouldStart)
    }

    fun runSharedOuttakeSequence(shouldStart: Boolean) {
        sharedOuttakeSequence.smartRun(shouldStart)
    }

    fun runCloseOuttakeSequence(shouldStart: Boolean) {
        closeOuttakeSequence.smartRun(shouldStart)
    }

    override fun update() {
        subsystems.forEach { it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.stop() }
    }
}
