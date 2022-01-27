package robotuprising.ftc2021.subsystems.osiris

import com.qualcomm.robotcore.hardware.DigitalChannel
import robotuprising.ftc2021.hardware.osiris.interfaces.Loopable
import robotuprising.ftc2021.hardware.osiris.interfaces.Readable
import robotuprising.ftc2021.hardware.osiris.interfaces.Zeroable
import robotuprising.ftc2021.manager.BulkDataManager

open class LimitSwitch(name: String, private val zeroableSubsystem: Zeroable) : Subsystem(), Readable, Loopable {
    private val limitSwitch = BulkDataManager.hwMap[DigitalChannel::class.java, name]

    private var pressed = false

    override fun read() {
        pressed = limitSwitch.state
    }

    override fun loop() {
        if(pressed) {
            zeroableSubsystem.zero()
        }
    }

    override fun stop() {

    }

    override fun updateDashboard(debugging: Boolean) {

    }
}