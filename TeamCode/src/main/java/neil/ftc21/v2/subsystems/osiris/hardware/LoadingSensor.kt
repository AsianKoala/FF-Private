package neil.ftc21.v2.subsystems.osiris.hardware

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import neil.ftc21.v2.hardware.osiris.interfaces.Readable
import neil.ftc21.v2.manager.BulkDataManager
import neil.ftc21.v2.subsystems.osiris.Subsystem
import neil.ftc21.v2.util.Constants

object LoadingSensor : Subsystem(), Readable {
    private val loadingSensor by lazy { BulkDataManager.hwMap[RevColorSensorV3::class.java, "loadingSensor"] }

    private var lastRead = Double.POSITIVE_INFINITY

    val isMineralIn get() = lastRead < Constants.loadingSensorThreshold

    override fun read() {
        lastRead = loadingSensor.getDistance(DistanceUnit.MM)
    }

    override fun stop() {
        lastRead = Double.POSITIVE_INFINITY
    }

    override fun updateDashboard(debugging: Boolean) {
//        OsirisDashboard.addLine()
//        OsirisDashboard["loading sensor"] = lastRead
    }
}