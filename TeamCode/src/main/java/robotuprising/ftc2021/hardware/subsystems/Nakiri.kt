package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem

/**
 * @see 254's Superstructure class
 */
object Nakiri : Subsystem() {

    private val driveManager = DriveManager()
    private val intake = Intake()
    private val lift = Lift()
    private val outtake = Outtake()
    private val duckSpinner = DuckSpinner()
    private val arm = MarkerGrabber()
    private val subsystems = mutableListOf(driveManager, intake, lift, outtake, duckSpinner, arm)

    private var defaultLiftLevel = 0

    override fun init(hwMap: HardwareMap) {
        Globals.hwMap = hwMap
        subsystems.forEach { it.init(hwMap) }
    }

    override fun init_loop() {
        subsystems.forEach { it.init_loop() }
    }

    override fun start() {
        subsystems.forEach { it.start() }
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

    private fun changeLiftDefault() {
        lift.changeDefault(Lift.LiftStages.values()[defaultLiftLevel])
    }

    fun requestDriveManagerPowers(powers: Pose) {
        driveManager.setPowers(powers)
    }

    fun requestDriveManagerStop() {
        driveManager.stop()
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

    fun requestIntakeSequence() {
        intake.runIntakeStateMachine()
    }

    fun requestIncrementDefaultLiftLevel() {
        if (defaultLiftLevel != Lift.MAX_LIFT_STAGE) {
            defaultLiftLevel++
            changeLiftDefault()
        }
    }

    fun requestDecrementDefaultLiftLevel() {
        if (defaultLiftLevel != Lift.MIN_LIFT_STAGE) {
            defaultLiftLevel--
            changeLiftDefault()
        }
    }

    fun requestLiftGoToDefault() {
        lift.setLevelToDefault()
    }

    fun requestLiftReset() {
        lift.setLevel(Lift.LiftStages.RESTING)
    }

    fun requestLiftShared() {
        lift.setLevel(Lift.LiftStages.SHARED)
    }

    fun requestEmergencyLiftControl(power: Double) {
        lift.emergencyControl(power)
    }

    fun requestDeposit() {
        TODO()
    }

    fun requestSpinnerOn() {
        duckSpinner.turnOn()
    }

    fun requestSpinnerOff() {
        duckSpinner.turnOff()
    }
}
