package robotuprising.ftc2021.hardware.osiris

import org.openftc.revextensions2.ExpansionHubServo
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.lib.math.MathUtil.epsilonNotEqual

class OsirisServo(name: String) {
    private val servo by lazy { BulkDataManager.hwMap[ExpansionHubServo::class.java, name] }

    var position: Double = 0.0
        set(value) {
            if (value epsilonNotEqual field) {
                servo.position = value
                field = value
            }
        }
}
