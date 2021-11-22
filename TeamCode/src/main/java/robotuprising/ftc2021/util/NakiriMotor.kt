package robotuprising.ftc2021.util

import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue

class NakiriMotor(private val name: String, private val onMaster: Boolean, private val motor: ExpansionHubMotor) {

    var power: Double = 0.0
        set(value) {
            val clipped = Range.clip(value, -1.0, 1.0)
            if (clipped != field && (clipped == 0.0 || clipped.absoluteValue == 1.0 || (clipped - field).absoluteValue > 0.005)) {
                field = value
                motor.power = value
            }
        }

    var targetPosition: Int = 0
        set(value) {
            if (value != field) {
                field = value
            }
        }

    val bulkPosition: Int
        get() {
            return if (onMaster) {
                BulkDataManager.masterData.getMotorCurrentPosition(Globals.MASTER_MAPPINGS.indexOf(name))
            } else {
                BulkDataManager.slaveData.getMotorCurrentPosition(Globals.SLAVE_MAPPINGS.indexOf(name))
            }
        }

    val position: Int get() = motor.currentPosition
}
