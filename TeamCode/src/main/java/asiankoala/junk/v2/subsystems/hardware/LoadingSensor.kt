package asiankoala.junk.v2.subsystems.hardware

import asiankoala.junk.v2.hardware.interfaces.Readable
import asiankoala.junk.v2.manager.BulkDataManager
import asiankoala.junk.v2.subsystems.Subsystem
import asiankoala.junk.v2.util.Constants
import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit

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
