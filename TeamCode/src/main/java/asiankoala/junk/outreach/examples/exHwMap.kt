package asiankoala.junk.outreach.examples

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class exHwMap(hardwareMap: HardwareMap) {
    var intake: DcMotor

    init {
        intake = hardwareMap.get(DcMotor::class.java, "intake")
    }
}
