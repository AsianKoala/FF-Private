package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import robotuprising.lib.system.Subsystem

/**
 * @see 254's Superstructure class
 */
object Akemi : Subsystem() {

    private val homura = Homura
    private val intake = Intake
    private val lift = Lift
    private val intakePivot = IntakePivot
    private val subsystems = mutableListOf(homura, intake, lift, intakePivot)

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

    fun requestHomuraPowers(dtPowers: MecanumPowers) {
        homura.setPowers(dtPowers)
    }

    fun requestsHomuraStop() {
        homura.stop()
    }

    fun requestIntakeOn() {
        intake.turnOn()
    }

    fun requestIntakeOff() {
        intake.turnOff()
    }

    fun requestIntakeReverse() {
        intake.reverse()
    }

    fun requestDefaultLiftTarget(stage: Lift.LiftStages) {
        lift.setDefaultTarget(stage)
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
        TODO()
    }

    fun requestEmergencyLiftControl(power: Double) {
        lift.emergencyControl(power)
    }

    fun requestStopLiftEmergency() {
        lift.status = Status.ALIVE
        requestLiftReset()
    }

    fun requestEmergencyBDSControl() {
        TODO()
    }
}
