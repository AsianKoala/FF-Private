package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import org.firstinspires.ftc.teamcode.hardware.BaseRobot
import robotuprising.lib.util.debug.Debuggable
import org.firstinspires.ftc.teamcode.util.opmode.Globals
import robotuprising.lib.util.OpModeType
import org.firstinspires.ftc.teamcode.util.opmode.Status

abstract class BaseOpMode : TunableLinearOpMode() {

    abstract val robot: BaseRobot

    private val status: Status
        get() = when {
            isStopRequested -> Status.STOP
            isStarted -> Status.LOOP
            else -> Status.INIT_LOOP
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
            when (status) {
                Status.INIT_LOOP -> {
                    robot.init_loop()
                }

                Status.LOOP -> {
                    if (hasStarted) {
                        robot.loop()
                    } else {
                        robot.start()
                        hasStarted = true
                    }
                }

                Status.STOP -> {
                    break@mainLoop
                }
            }
        }

        robot.stop()
        if (opModeType == OpModeType.AUTO && Globals.IS_COMP)
            manager.initActiveOpMode(Globals.TELEOP_NAME)
    }
}
