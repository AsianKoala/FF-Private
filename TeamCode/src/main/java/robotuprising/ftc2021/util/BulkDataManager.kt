package robotuprising.ftc2021.util

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap
import org.openftc.revextensions2.ExpansionHubEx
import org.openftc.revextensions2.RevBulkData

object BulkDataManager {
    lateinit var hwMap: HardwareMap
    private lateinit var masterHub: ExpansionHubEx
    private lateinit var slaveHub: ExpansionHubEx
    lateinit var masterData: RevBulkData
    lateinit var slaveData: RevBulkData

    fun init(hardwareMap: HardwareMap) {
        hwMap = hardwareMap
        masterHub = hwMap[ExpansionHubEx::class.java, "masterHub"]
        slaveHub = hwMap[ExpansionHubEx::class.java, "slaveHub"]

        hardwareMap.getAll(LynxModule::class.java).forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.AUTO }
        read()
    }

    fun read() {
        masterData = masterHub.bulkInputData
        slaveData = slaveHub.bulkInputData
    }
}
