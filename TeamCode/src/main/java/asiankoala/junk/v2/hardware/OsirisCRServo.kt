package asiankoala.junk.v2.hardware

import asiankoala.junk.v2.lib.math.MathUtil.epsilonNotEqual
import asiankoala.junk.v2.manager.BulkDataManager
import com.qualcomm.robotcore.hardware.CRServo

class OsirisCRServo(name: String) {
    private val servo = BulkDataManager.hwMap[CRServo::class.java, name]

    var power: Double = 0.0
        set(value) {
            if (field epsilonNotEqual value) {
                servo.power = value
                field = value
            }
        }
}
