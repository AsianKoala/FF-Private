package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
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

    private lateinit var opModeType: OpModeType
    private var debugging = false

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
        if (opModeType == OpModeType.AUTO && is_comp) {
            val opName = manager.activeOpModeName.substring(0, manager.activeOpModeName.indexOf("Auto"))
            manager.initActiveOpMode(opName + "Auto")
        }
    }

    private fun debuggingTelem() {
        telemetry.addData("debugging", debugging)
        telemetry.addData("opModeType", opModeType)
        telemetry.addData("has started", hasStarted)
        telemetry.addData("is comp", is_comp)
    }

    abstract val is_comp: Boolean
    abstract fun m_init()
    abstract fun m_init_loop()
    abstract fun m_start()
    abstract fun m_loop()
    abstract fun m_stop()
}
