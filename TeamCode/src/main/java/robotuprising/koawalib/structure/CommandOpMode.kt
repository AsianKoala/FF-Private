package robotuprising.koawalib.structure

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.koawalib.command.scheduler.CommandScheduler
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.manager.KoawaBulkManager
import robotuprising.koawalib.manager.KoawaDashboard

open class CommandOpMode : LinearOpMode() {

    lateinit var driverGamepad: CommandGamepad
    lateinit var gunnerGamepad: CommandGamepad

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

    var disabled = false

    override fun runOpMode() {
        CommandScheduler.resetScheduler()
        CommandScheduler.setOpMode(this)

        KoawaDashboard.init(telemetry, false)
        KoawaBulkManager.init(hardwareMap)

        driverGamepad = CommandGamepad(gamepad1)
        gunnerGamepad = CommandGamepad(gamepad2)

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
            driverGamepad.periodic()
            gunnerGamepad.periodic()

            KoawaBulkManager.clear()
            KoawaDashboard.update()
        }


        mStop()

        CommandScheduler.resetScheduler()
        opModeTimer.reset()
    }


    open fun mInit() {}
    open fun mInitLoop() {}
    open fun mStart() {}
    open fun mLoop() {}
    open fun mStop() {}
    open fun mUniversal() {}


    fun terminate() {
        terminate = true
    }
}