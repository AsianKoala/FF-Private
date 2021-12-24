package robotuprising.ftc2021.util

import com.qualcomm.robotcore.hardware.CRServo
import robotuprising.lib.math.MathUtil.epsilonNotEqual

class WraithCRServo(name: String) {
    private val servo = BulkDataManager.hwMap[CRServo::class.java, name]

    var power: Double = 0.0
        set(value) {
            if(field epsilonNotEqual value) {
                servo.power = value
                field = value
            }
        }
}