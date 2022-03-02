package neil.ftc21.v2.hardware.osiris

import org.openftc.revextensions2.ExpansionHubServo
import neil.ftc21.v2.manager.BulkDataManager
import neil.lib.math.MathUtil.epsilonNotEqual

class OsirisServo(name: String) {
    private val servo = BulkDataManager.hwMap[ExpansionHubServo::class.java, name]

    var position: Double = -1.0
        set(value) {
            if(value == -1.0) {
                field = value
            } else if (value epsilonNotEqual field) {
                servo.position = value
                field = value
            }
        }
}
