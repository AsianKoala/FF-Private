package robotuprising.ftc2021.hardware.subsystems

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

    var isReading = false
        private set
    private var hasBeenRead = false
    var forwardReading = 0
        private set
    var horizontalReading = 0
        private set

    fun startReading() {
        isReading = true
    }

    fun stopReading() {
        isReading = false
    }

    val finishedReadInterval: Boolean get() = timer.milliseconds() > readingInterval

    override fun update() {
        if (isReading) {
            if (finishedReadInterval && !hasBeenRead) {
                forwardReading = forward.readRangeValueCm()
                horizontalReading = horizontal.readRangeValueCm()
                timer.reset()
                hasBeenRead = true
            } else {
                forward.initiateRangeCommand()
                horizontal.initiateRangeCommand()
            }
        }
    }

    override fun stop() {
    }

    override fun sendDashboardPacket(debugging: Boolean) {
        NakiriDashboard["last forward value"] = forwardReading
        NakiriDashboard["last horizontal value"] = horizontalReading
    }
}
