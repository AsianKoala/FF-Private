package robotuprising.koawalib.command

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.koawalib.command.scheduler.CommandScheduler
import robotuprising.koawalib.manager.KoawaBulkManager
import robotuprising.koawalib.manager.KoawaDashboard
import robotuprising.lib.opmode.OpModeState
import robotuprising.lib.opmode.OsirisDashboard

class CommandOpMode : LinearOpMode() {
    val opModeState: OpModeState
        get() = when {
            isStopRequested -> OpModeState.STOP
            isStarted -> OpModeState.LOOP
            hasInitYet -> OpModeState.INIT_LOOP
            else -> OpModeState.INIT
        }

    private var hasInitYet = false
    private var hasStarted = false
    private var prevLoopTime = System.currentTimeMillis()

    private var terminate = false

    private var opModeTimer = ElapsedTime()

    val opModeRuntime get() = opModeTimer.seconds()

    override fun runOpMode() {
        CommandScheduler.resetScheduler()

        OsirisDashboard.init(telemetry, false)

        KoawaBulkManager.init(hardwareMap)
        opModeTimer.reset()


        mainLoop@ while(true) {
            when(opModeState) {
                OpModeState.INIT -> {
                    mInit()
                    hasInitYet = true
                }

                OpModeState.INIT_LOOP -> {
                    mInitLoop()
                    mUniversal()
                }

                OpModeState.LOOP -> {
                    if(hasStarted) {
                        val dt = System.currentTimeMillis() - prevLoopTime
                        telemetry.addData("loop ms", dt)
                        mLoop()
                        mUniversal()
                    } else {
                        mStart()
                        opModeTimer.reset()
                        hasStarted = true
                    }
                }

                OpModeState.STOP -> {
                    break@mainLoop
                }
            }

            if(terminate) {
                break@mainLoop
            }

            prevLoopTime = System.currentTimeMillis()

            CommandScheduler.run()

            KoawaBulkManager.clear()
            KoawaDashboard.update()
        }


        mStop()

        CommandScheduler.resetScheduler()
        opModeTimer.reset()
    }


    fun mInit() {}
    fun mInitLoop() {}
    fun mStart() {}
    fun mLoop() {}
    fun mStop() {}
    fun mUniversal() {}


    fun terminate() {
        terminate = true
    }
}