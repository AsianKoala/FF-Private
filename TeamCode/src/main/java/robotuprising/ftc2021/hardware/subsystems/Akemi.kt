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
    private val subsystems = mutableListOf(homura, intake)

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
        subsystems.forEach { if(it.status != Status.DEAD ) it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.stop() }
    }

    override var status: Status = Status.ALIVE

    fun requestHomuraPowers(dtPowers: MecanumPowers) {
        Homura.setPowers(dtPowers)
    }

    fun requestsHomuraStop() {
        Homura.stop()
    }

    fun requestLiftLowAlliance() {
        lift.setLevel(Lift.LiftStages.ALLIANCE_LOW)
    }

    fun requestLiftMediumAlliance() {
        lift.setLevel(Lift.LiftStages.ALLIANCE_MEDIUM)
    }

    fun requestLiftHighAlliance() {
        lift.setLevel(Lift.LiftStages.ALLIANCE_HIGH)
    }

    fun requestLiftDefault() {
        lift.setLevel(Lift.LiftStages.DEFAULT)
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

    fun requestEmergencyBDSControl() {
        TODO()
    }

    fun requestEmergencyIntakeControl(power: Double) {
        intake.status = Status.EMERGENCY
        intake.customControl(power)
    }
}
