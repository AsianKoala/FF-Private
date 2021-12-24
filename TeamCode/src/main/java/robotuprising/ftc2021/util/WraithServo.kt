package robotuprising.ftc2021.util

import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.lib.math.MathUtil.epsilonNotEqual

class WraithServo(name: String) {
    private val servo = BulkDataManager.hwMap[ExpansionHubServo::class.java, name]

    var position: Double = 0.0
        set(value) {
            if (value epsilonNotEqual field) {
                servo.position = value
                field = value
            }
        }
}