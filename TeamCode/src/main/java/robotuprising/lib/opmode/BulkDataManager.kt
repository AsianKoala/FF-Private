package robotuprising.lib.opmode

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubEx

class BulkDataManager(hwMap: HardwareMap, private val masterHub: ExpansionHubEx, private val slaveHub: ExpansionHubEx) {

    private var masterBulkData = masterHub.bulkInputData
    private var slaveBulkData = slaveHub.bulkInputData
    lateinit var masterNameToPort: MutableList<String>
    lateinit var slaveNameToPort: MutableList<String>

    fun get(name: String) {

    }

    fun update() {
        masterBulkData = masterHub.bulkInputData
        slaveBulkData = slaveHub.bulkInputData
    }

    init {
        for(motor in hwMap.dcMotor) {
            masterNameToPort
        }
    }
}