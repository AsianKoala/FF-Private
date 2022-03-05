package asiankoala.junk.v2.subsystems

import asiankoala.junk.v2.hardware.interfaces.Loopable
import asiankoala.junk.v2.hardware.interfaces.Readable
import asiankoala.junk.v2.hardware.interfaces.Zeroable
import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.manager.BulkDataManager
import com.qualcomm.robotcore.hardware.DigitalChannel

open class LimitSwitch(private val name: String, private val zeroableSubsystem: Zeroable) :
    Subsystem(),
    Readable,
    Loopable {
    private val limitSwitch by lazy { BulkDataManager.hwMap[DigitalChannel::class.java, name] }

    private var pressed = false

    override fun read() {
        pressed = limitSwitch.state
    }

    override fun loop() {
//        if(pressed) {
//            zeroableSubsystem.zero()
//        }
    }

    override fun stop() {
    }

    override fun updateDashboard(debugging: Boolean) {
        OsirisDashboard["$name pressed"] = pressed
    }
}
