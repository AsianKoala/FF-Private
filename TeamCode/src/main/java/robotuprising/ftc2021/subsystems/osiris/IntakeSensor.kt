package robotuprising.ftc2021.subsystems.osiris

import com.qualcomm.hardware.rev.RevColorSensorV3
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import robotuprising.ftc2021.hardware.osiris.interfaces.Readable
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.ftc2021.util.Constants

object IntakeSensor : Subsystem(), Readable {
    private val intakeSensor = BulkDataManager.hwMap[RevColorSensorV3::class.java, "intakeSensor"]

    private var lastRead = Double.POSITIVE_INFINITY

    val isMineralIn get() = lastRead < Constants.intakeSensorThreshold

    override fun read() {
        lastRead = intakeSensor.getDistance(DistanceUnit.MM)
    }

    override fun reset() {

    }

    override fun updateDashboard(debugging: Boolean) {

    }

}