package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotuprising.ftc2021.manager.BulkDataManager
import robotuprising.lib.debug.Debuggable
import robotuprising.lib.opmode.*

// todo fix alliance side maybe idk
abstract class BaseOpMode : LinearOpMode() {
    private val opModeState: OpModeState
        get() = when {
            isStopRequested -> OpModeState.STOP
            isStarted -> OpModeState.LOOP
            else -> OpModeState.INIT_LOOP
        }

    private var hasStarted = false
    private var prevLoopTime = System.currentTimeMillis()

    private var debugging = false

    protected var allianceSide = AllianceSide.BLUE

    override fun runOpMode() {
        debugging = javaClass.isAnnotationPresent(Debuggable::class.java)

        allianceSide = if(javaClass.isAnnotationPresent(BlueAlliance::class.java)) {
            AllianceSide.BLUE
        } else {
            AllianceSide.RED
        }

        BulkDataManager.init(hardwareMap)
        OsirisDashboard.init(telemetry, false)


        mInit()

        mainLoop@ while (true) {
            BulkDataManager.read()

            when (opModeState) {
                OpModeState.INIT_LOOP -> {
                    mInitLoop()
                }

                OpModeState.LOOP -> {
                    if (hasStarted) {
                        val dt = System.currentTimeMillis() - prevLoopTime
                        telemetry.addData("loop ms", dt)
                        mLoop()
                    } else {
                        mStart()
                        hasStarted = true
                    }
                    prevLoopTime = System.currentTimeMillis()
                }

                OpModeState.STOP -> {
                    break@mainLoop
                }
            }

            OsirisDashboard.update()
        }

        mStop()
    }

    abstract fun mInit()
    abstract fun mInitLoop()
    abstract fun mStart()
    abstract fun mLoop()
    abstract fun mStop()
}
