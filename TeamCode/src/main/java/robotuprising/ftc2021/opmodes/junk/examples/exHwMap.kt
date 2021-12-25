package robotuprising.ftc2021.opmodes.junk.examples

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.DcMotor

class exHwMap(hardwareMap: HardwareMap) {
    var intake: DcMotor

    init {
        intake = hardwareMap.get(DcMotor::class.java, "intake")
    }
}