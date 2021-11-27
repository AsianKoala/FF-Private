package robotuprising.ftc2021.subsystems

import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.lib.hardware.MB1242
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

class Ultrasonics : Subsystem {
    private val forward: MB1242 = BulkDataManager.hwMap[MB1242::class.java, "forwardSensor"]
    private val horizontal: MB1242 = BulkDataManager.hwMap[MB1242::class.java, "sideSensor"]

    private val timer = ElapsedTime()
    private val readingInterval = 100

    private var isReading = false
    var hasBeenRead = false
        private set
    var forwardReading = 0
        private set
    var horizontalReading = 0
        private set

    fun startReading() {
        isReading = true
    }

    /**
     * TODO: FIX THIS
     */
    val finishedReadInterval: Boolean get() = timer.milliseconds() > readingInterval

    // sensor will only read once to ensure i2c isn't called multiple times
    override fun update() {
        if (isReading) {
            if (finishedReadInterval && !hasBeenRead) {
                forwardReading = forward.readRangeValueCm()
                horizontalReading = horizontal.readRangeValueCm()
                timer.reset()
                hasBeenRead = true
                isReading = false
            } else {
                forward.initiateRangeCommand()
                horizontal.initiateRangeCommand()
                hasBeenRead = false
            }
        }
    }

    override fun reset() {
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard["last forward value"] = forwardReading
        NakiriDashboard["last horizontal value"] = horizontalReading
    }
}
