package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubEx
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import robotuprising.lib.system.Subsystem

/**
 * @see 254's Superstructure class
 */
object Nakiri : Subsystem() {

    private val driveManager = DriveManager
    private val intake = Intake
    private val lift = Lift
    private val subsystems = mutableListOf(driveManager, intake, lift)

    private var defaultLiftLevel = 0

    override fun init(hwMap: HardwareMap) {
        subsystems.forEach { it.init(hwMap) }
    }

    override fun init_loop() {
        subsystems.forEach { it.init_loop() }
    }

    override fun start() {
        subsystems.forEach { it.start() }
    }

    override fun update() {
        subsystems.forEach { if (it.status != Status.DEAD) it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.stop() }
    }

    override var status: Status = Status.ALIVE


    private fun changeLiftDefault() {
        lift.setDefaultTarget(Lift.LiftStages.values()[defaultLiftLevel])
    }

    fun requestDriveManagerPowers(dtPowers: MecanumPowers) {
        driveManager.setHomuraVectors(dtPowers)
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

    fun requestIncrementDefaultLiftLevel() {
        if(defaultLiftLevel != Lift.MAX_LIFT_STAGE) defaultLiftLevel++
        changeLiftDefault()
    }

    fun requestDecrementDefaultLiftLevel() {
        if(defaultLiftLevel != Lift.MIN_LIFT_STAGE) defaultLiftLevel--
        changeLiftDefault()
    }

    fun requestLiftGoToDefault() {
        lift.goToDefault()
    }

    fun requestLiftReset() {
        lift.setLevel(Lift.LiftStages.RESTING)
    }

    fun requestLiftShared() {
        lift.setLevel(Lift.LiftStages.SHARED)
    }

    fun requestDeposit() {

    }

    fun requestFullIntakeSequence() {

    }

    fun requestEmergencyLiftControl(power: Double) {
        lift.emergencyControl(power)
    }

    fun requestStopLiftEmergency() {
        lift.status = Status.ALIVE
        requestLiftReset()
    }
}
