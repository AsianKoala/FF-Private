package robotuprising.lib.system

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import robotuprising.lib.debug.Debuggable
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.BlueAlliance
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.opmode.OpModeStatus

abstract class BaseOpMode : LinearOpMode() {
    private val opModeStatus: OpModeStatus
        get() = when {
            isStopRequested -> OpModeStatus.STOP
            isStarted -> OpModeStatus.LOOP
            else -> OpModeStatus.INIT_LOOP
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



        mInit()

        mainLoop@ while (true) {
            when (opModeStatus) {
                OpModeStatus.INIT_LOOP -> {
                    mInitLoop()
                }

                OpModeStatus.LOOP -> {
                    if (hasStarted) {
                        val dt = System.currentTimeMillis() - prevLoopTime
                        telemetry.addData("loop ms", dt)
                        prevLoopTime = System.currentTimeMillis()
                        mLoop()
                    } else {
                        mStart()
                        hasStarted = true
                    }
                }

                OpModeStatus.STOP -> {
                    break@mainLoop
                }
            }

            NakiriDashboard.update()
        }

        mStop()
    }

    abstract fun mInit()
    open fun mInitLoop() {}
    open fun mStart() {}
    abstract fun mLoop()
    open fun mStop() {}
    open fun mTest() {}
}
