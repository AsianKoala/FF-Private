package robotuprising.koawalib.manager

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.hardware.HardwareMap

object KoawaBulkManager {
    private lateinit var hubs: List<LynxModule>

    fun init(hardwareMap: HardwareMap) {
        hubs = hardwareMap.getAll(LynxModule::class.java)

        hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL }
    }

    fun clear() {
        hubs.forEach(LynxModule::clearBulkCache);
    }
}