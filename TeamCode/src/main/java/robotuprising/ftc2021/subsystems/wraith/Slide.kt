package robotuprising.ftc2021.subsystems.wraith

import com.qualcomm.robotcore.hardware.DigitalChannel
import robotuprising.ftc2021.util.BulkDataManager
import robotuprising.ftc2021.util.WraithMotor

class Slide : Subsystem {
    private val slideMotor = WraithMotor("slide", false).float.openLoopControl
    private val limitSwitch = BulkDataManager.hwMap[DigitalChannel::class.java, "limitSwitch"]

    private var pos = 0.0
    private var target = 0.0
    private var output = 0.0


}