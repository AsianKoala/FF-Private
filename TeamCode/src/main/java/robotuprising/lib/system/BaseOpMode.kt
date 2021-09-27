package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import robotuprising.ftc2021.hardware.BaseRobot
import robotuprising.lib.util.debug.Debuggable
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.util.opmode.OpModeType
import robotuprising.lib.util.opmode.OpModeStatus

abstract class BaseOpMode : TunableLinearOpMode() {

    abstract val robot: BaseRobot

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

        robot.init()
        mainLoop@ while (true) {
            when (opModeStatus) {
                OpModeStatus.INIT_LOOP -> {
                    robot.init_loop()
                }

                OpModeStatus.LOOP -> {
                    if (hasStarted) {
                        robot.loop()
                    } else {
                        robot.start()
                        hasStarted = true
                    }
                }

                OpModeStatus.STOP -> {
                    break@mainLoop
                }
            }
        }

        robot.stop()
        val autoName = manager.activeOpModeName.substring(0,manager.activeOpModeName.indexOf("TeleOp"))
        if (opModeType == OpModeType.AUTO && Globals.IS_COMP)
            manager.initActiveOpMode(autoName + "Auto")
    }
}
