package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.debug.Debuggable
import robotuprising.lib.opmode.OpModeStatus
import robotuprising.lib.opmode.OpModeType

abstract class BaseOpMode : TunableLinearOpMode() {

    private val opModeStatus: OpModeStatus
        get() = when {
            isStopRequested -> OpModeStatus.STOP
            isStarted -> OpModeStatus.LOOP
            else -> OpModeStatus.INIT_LOOP
        }

    private var hasStarted = false

    lateinit var opModeType: OpModeType
    var debugging = false

    override fun runOpMode() {
        val manager = (internalOpModeServices as OpModeManagerImpl)

        debugging = javaClass.isAnnotationPresent(Debuggable::class.java)
        opModeType = if (javaClass.isAnnotationPresent(Autonomous::class.java)) {
            OpModeType.AUTO
        } else {
            OpModeType.TELEOP
        }

        m_init()
        mainLoop@ while (true) {
            when (opModeStatus) {
                OpModeStatus.INIT_LOOP -> {
                    m_init_loop()
                }

                OpModeStatus.LOOP -> {
                    if (hasStarted) {
                        m_loop()
                    } else {
                        m_start()
                        hasStarted = true
                    }
                }

                OpModeStatus.STOP -> {
                    break@mainLoop
                }
            }
        }

        m_stop()
        if (opModeType == OpModeType.AUTO && Globals.IS_COMP) {
            val opName = manager.activeOpModeName.substring(0, manager.activeOpModeName.indexOf("Auto"))
            manager.initActiveOpMode(opName + "Auto")
        }
    }

    abstract fun m_init()
    abstract fun m_init_loop()
    abstract fun m_start()
    abstract fun m_loop()
    abstract fun m_stop()
}
