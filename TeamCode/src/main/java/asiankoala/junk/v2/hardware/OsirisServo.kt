package asiankoala.junk.v2.hardware

import asiankoala.junk.v2.lib.math.MathUtil.epsilonNotEqual
import asiankoala.junk.v2.manager.BulkDataManager
import org.openftc.revextensions2.ExpansionHubServo

class OsirisServo(name: String) {
    private val servo = BulkDataManager.hwMap[ExpansionHubServo::class.java, name]

    var position: Double = -1.0
        set(value) {
            if (value == -1.0) {
                field = value
            } else if (value epsilonNotEqual field) {
                servo.position = value
                field = value
            }
        }
}
