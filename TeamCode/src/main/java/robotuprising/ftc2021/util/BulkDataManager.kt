package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubEx
import org.openftc.revextensions2.RevBulkData

// todo
object BulkDataManager {
    var hwMap: HardwareMap = Globals.hwMap

    private val masterHub: ExpansionHubEx = hwMap[ExpansionHubEx::class.java, "masterHub"]
    private val slaveHub: ExpansionHubEx = hwMap[ExpansionHubEx::class.java, "slaveHub"]

    var masterData: RevBulkData = masterHub.bulkInputData
    var slaveData: RevBulkData = slaveHub.bulkInputData

    fun read() {
        masterData = masterHub.bulkInputData
        slaveData = slaveHub.bulkInputData
    }
}
