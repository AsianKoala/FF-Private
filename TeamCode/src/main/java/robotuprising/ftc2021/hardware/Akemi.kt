package robotuprising.ftc2021.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.hardware.Accuracy
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import robotuprising.lib.system.Subsystem

/**
 * @see 254's Superstructure class
 */
object Akemi : Subsystem() {

    private val homura = Homura
    private val intake = Intake
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
        subsystems.forEach { it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override var status: Status = Status.RUNNING
    override var acc: Accuracy = Accuracy.HIGH

    fun requestIntakeOn() {
        intake.turnOn()
    }

    fun requestIntakeOff() {
        intake.turnOff()
    }

    fun requestIntakeReverse() {
        intake.reverse()
    }

    fun requestHomuraPowers(dtPowers: MecanumPowers) {
        homura.setPowers(dtPowers)
    }

    fun requestsHomuraStop() {
        homura.stop()
    }
}
