package robotuprising.ftc2021.hardware.subsystems

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
        subsystems.forEach { if(it.status==Status.ALIVE) it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.stop() }
    }

    override var status: Status = Status.ALIVE
    override var acc: Accuracy = Accuracy.HIGH

    fun requestIntakeOn() {
        Intake.turnOn()
    }

    fun requestIntakeOff() {
        Intake.turnOff()
    }

    fun requestIntakeReverse() {
        Intake.reverse()
    }

    fun requestHomuraPowers(dtPowers: MecanumPowers) {
        Homura.setPowers(dtPowers)
    }

    fun requestsHomuraStop() {
        Homura.stop()
    }
}
