package robotuprising.ftc2021.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.system.Subsystem
import robotuprising.lib.util.hardware.Status

/**
 * @see 254's 2017 Superstructure.java class
 */
class Superstructure : Subsystem() {

    private val homura = Homura()
    private val intake = Intake()
    private val subsystems = listOf(homura, intake)

    override fun init(hwMap: HardwareMap) {
        subsystems.forEach { it.init(hwMap) }
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

    override val status: Status
        get() {
            subsystems.forEach {
                when(it.status) {
                    Status.STALLING -> return Status.STALLING
                    Status.FAILING -> return Status.FAILING
                    Status.DEAD -> return Status.DEAD
                    else -> {}
                }
            }
            return Status.WORKING
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

    fun requestHomuraPowers(dtPowers: `6WDPowers`) {
        homura.setPowers(dtPowers)
    }

    fun requestsHomuraStop() {
        homura.stop()
    }
}
