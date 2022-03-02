package neil.ftc21.v2.hardware.nakiri

import org.openftc.revextensions2.ExpansionHubServo
import neil.ftc21.v2.manager.BulkDataManager

class NakiriServo(name: String) {
//    private val servo = Globals.hwMap[ExpansionHubServo::class.java, name]
    private val servo = BulkDataManager.hwMap[ExpansionHubServo::class.java, name]

    var position: Double = 0.0
        set(value) {
            if (value != field) {
                servo.position = value
                field = value
            }
        }
}
