package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.Range
import org.openftc.revextensions2.ExpansionHubMotor
import kotlin.math.absoluteValue

class NakiriMotor(name: String, private val onMaster: Boolean) {
    private val motor = Globals.hwMap[ExpansionHubMotor::class.java, name]

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

    var zeroPowerBehavior: DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

    var mode: DcMotor.RunMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

    var direction: DcMotorSimple.Direction = DcMotorSimple.Direction.FORWARD

    val position: Int
        get() {
            return if(onMaster) {
                BulkDataManager.masterData.getMotorCurrentPosition(motor)
            } else {
                BulkDataManager.slaveData.getMotorCurrentPosition(motor)
            }
        }
}
