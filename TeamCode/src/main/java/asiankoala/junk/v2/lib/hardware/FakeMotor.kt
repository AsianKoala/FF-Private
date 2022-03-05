package asiankoala.junk.v2.lib.hardware

import asiankoala.junk.v2.manager.BulkDataManager
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.openftc.revextensions2.ExpansionHubMotor

class FakeMotor(val name: String, val onMaster: Boolean) {
    var builtYet = false
    var mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    var direction = DcMotorSimple.Direction.FORWARD
    var zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    var power = 0.0
        set(value) {
            if (!builtYet) {
                realMotor = BulkDataManager.hwMap[ExpansionHubMotor::class.java, name]
            }

            field = value
            realMotor.power = field
        }

    var position = 0
        get() {
            if (!builtYet) {
                realMotor = BulkDataManager.hwMap[ExpansionHubMotor::class.java, name]
            }

            return realMotor.currentPosition
        }

    var velocity = 0.0
        get() {
            if (!builtYet) {
                realMotor = BulkDataManager.hwMap[ExpansionHubMotor::class.java, name]
            }

            return realMotor.velocity
        }

    private lateinit var realMotor: ExpansionHubMotor
}
