package robotuprising.ftc2021.subsystems

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem
import robotuprising.lib.system.statemachine.StateMachineBuilder

class Nakiri : Subsystem {

    // 10 opacity
    val ayame = Ayame()
    val intake = Intake()
    val lift = Lift()
    val linkage = Linkage()
    val outtake = Outtake()
    val duckSpinner = DuckSpinner()
    val subsystems = mutableListOf(
        ayame,
        intake,
        lift,
        linkage,
        outtake,
        duckSpinner,
    )

    val currPose get() = ayame.pose

    val intaking get() = intakeSequence.running

    val outtaking get() = sharedOuttakeSequence.running || closeOuttakeSequence.running || longOuttakeSequence.running

    val inCrater get() = ayame.locationState == Ayame.LocationStates.CRATER
    val inField get() = ayame.locationState == Ayame.LocationStates.FIELD

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
        LINKAGE_OUT,
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
            requestLiftTransfer()
            requestLinkageTransfer()
        }
        .transitionTimed(1.5)
        .state(IntakeSequenceStates.TRANSFERRING)
        .onEnter {
            requestIntakeReverse()
        }
        .transitionTimed(1.5)
        .state(IntakeSequenceStates.OUTTAKE_TO_MEDIUM)
        .onEnter {
            requestOuttakeMedium()
            requestIntakeOff()
        }
        .transition { true }
        .build()

    private val sharedOuttakeSequence = StateMachineBuilder<SharedOuttakeState>()
        .state(SharedOuttakeState.LINKAGE_OUT)
        .onEnter {
            requestLinkageMedium()
        }
        .transitionTimed(0.5)
        .state(SharedOuttakeState.OUTTAKE_OUT)
        .onEnter { requestOuttakeOut() }
        .transitionTimed(0.75)
        .state(SharedOuttakeState.OUTTAKE_IN)
        .onEnter {
            requestOuttakeIn()
            requestLinkageRetract()
        }
        .transition { true }
        .build()

    private val closeOuttakeSequence = StateMachineBuilder<OuttakeSequenceStates>()
        .state(OuttakeSequenceStates.LIFT_UP)
        .onEnter {
            requestLiftHigh()
        }
        .transitionTimed(1.25)
        .state(OuttakeSequenceStates.LINKAGE_OUT)
        .onEnter { requestLinkageMedium() }
        .transitionTimed(0.5)
        .state(OuttakeSequenceStates.DEPOSIT)
        .onEnter { requestOuttakeOut() }
        .transitionTimed(0.75)
        .state(OuttakeSequenceStates.OUTTAKE_DOWN_LINKAGE_IN)
        .onEnter { requestOuttakeIn(); requestLinkageRetract() }
        .transitionTimed(0.5)
        .state(OuttakeSequenceStates.RESET)
        .onEnter { requestLiftBottom() }
        .transition { true }
        .build()

    private enum class OuttakeLongStates {
        LIFTING,
        EXTENDING_AND_WAIT,
        DEPOSIT,
        RETRACT_LINKAGE_AND_OUTTAKE,
        LIFT_DOWN
    }

    private var outtakeLongSequenceTransition = false
    private val longOuttakeSequence = StateMachineBuilder<OuttakeLongStates>()
            .state(OuttakeLongStates.LIFTING)
            .onEnter {
                requestLiftHigh()
            }
            .transitionTimed(0.4)
            .state(OuttakeLongStates.EXTENDING_AND_WAIT)
            .onEnter { requestLinkageOut() }
            .transition { outtakeLongSequenceTransition }
            .state(OuttakeLongStates.DEPOSIT)
            .onEnter { requestOuttakeOut() }
            .transitionTimed(0.5)
            .state(OuttakeLongStates.RETRACT_LINKAGE_AND_OUTTAKE)
            .onEnter {
                requestOuttakeIn();
                requestLinkageRetract()
            }
            .transitionTimed(0.5)
            .state(OuttakeLongStates.LIFT_DOWN)
            .onEnter { requestLiftBottom() }
            .transition { true }
            .build()


    fun requestAyamePowers(x: Double, y: Double, h: Double) {
        requestAyamePowers(Pose(Point(x, y), Angle(h, AngleUnit.RAW)))
    }

    fun requestAyamePowers(powers: Pose) {
        ayame.setVectorPower(powers)
    }

    fun requestAyameStop() {
        requestAyamePowers(Pose.DEFAULT_RAW)
    }

    fun startGoingOverPipes() {
        ayame.startGoingOverPipes()
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

    fun requestLiftBottom() {
        lift.setLevel(Lift.LiftStages.BOTTOM)
    }

    fun requestLiftTransfer() {
        lift.setLevel(Lift.LiftStages.TRANSFER)
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

    fun requestLinkageTransfer() {
        linkage.extendTransfer()
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
        if (!intakeSequence.running) {
            sharedOuttakeSequence.smartRun(shouldStart)
        } else if(sharedOuttakeSequence.running) {
            sharedOuttakeSequence.update()
        }
    }

    fun runCloseOuttakeSequence(shouldStart: Boolean) {
        if (!intakeSequence.running) {
            closeOuttakeSequence.smartRun(shouldStart)
        } else if(closeOuttakeSequence.running) {
            closeOuttakeSequence.update()
        }
    }

    fun runAutoOuttake(shouldStart: Boolean) {
        longOuttakeSequence.smartRun(shouldStart)
    }

    fun runTeleLongOuttakeSequence(shouldStart: Boolean) {
        if(!intakeSequence.running) {
            if(!longOuttakeSequence.running && shouldStart) {
                longOuttakeSequence.reset()
                outtakeLongSequenceTransition = false
                longOuttakeSequence.start()
            } else if(longOuttakeSequence.running && shouldStart) {
                outtakeLongSequenceTransition = true
            }

            if(longOuttakeSequence.running) {
                longOuttakeSequence.update()
            }
        } else if(longOuttakeSequence.running) {
            longOuttakeSequence.update()
        }
    }

    override fun update() {
        subsystems.forEach { it.update() }
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        subsystems.forEach { it.sendDashboardPacket(debugging) }
    }

    override fun reset() {
        subsystems.forEach { it.reset() }
    }
}
