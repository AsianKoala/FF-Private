package robotuprising.koawalib.command

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import robotuprising.koawalib.gamepad.CommandGamepad
import robotuprising.koawalib.logger.KoawaDashboard
import robotuprising.koawalib.statemachine.StateMachineBuilder
import robotuprising.koawalib.util.OpModeState

open class CommandOpMode : LinearOpMode() {
    protected lateinit var driverGamepad: CommandGamepad
    protected lateinit var gunnerGamepad: CommandGamepad

    private var prevLoopTime = System.currentTimeMillis()
    private var opModeTimer = ElapsedTime()
    private lateinit var hubs: List<LynxModule>

    var disabled = false
    val isLooping get() = mainStateMachine.state == OpModeState.LOOP

    private fun setup() {
        CommandScheduler.resetScheduler()
        CommandScheduler.setOpMode(this)
        KoawaDashboard.init(telemetry, false)

        hubs = hardwareMap.getAll(LynxModule::class.java)
        hubs.forEach { it.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL }

        driverGamepad = CommandGamepad(gamepad1)
        gunnerGamepad = CommandGamepad(gamepad2)
        opModeTimer.reset()
    }

    private fun schedulePeriodics() {
        CommandScheduler.addPeriodic { prevLoopTime = System.currentTimeMillis() }
        CommandScheduler.addPeriodic(driverGamepad::periodic)
        CommandScheduler.addPeriodic(gunnerGamepad::periodic)
        CommandScheduler.addPeriodic { hubs.forEach(LynxModule::clearBulkCache) }
        CommandScheduler.addPeriodic(KoawaDashboard::update)
    }

    private fun handleLoopMsTelemetry() {
        val dt = System.currentTimeMillis() - prevLoopTime
        telemetry.addData("loop ms", dt)
    }

    private val mainStateMachine = StateMachineBuilder<OpModeState>()
            .universal(CommandScheduler::run)

            .state(OpModeState.INIT)
            .onEnter(::setup)
            .onEnter(::schedulePeriodics)
            .onEnter(::mInit)
            .transition { true }

            .state(OpModeState.INIT_LOOP)
            .loop(::mInitLoop)
            .loop(::mUniversal)
            .transition(::isStarted)

            .state(OpModeState.START)
            .onEnter(::mStart)
            .onEnter(opModeTimer::reset)
            .transition { true }

            .state(OpModeState.LOOP)
            .loop(::mLoop)
            .loop(::mUniversal)
            .loop(::handleLoopMsTelemetry)
            .transition(::isStopRequested)

            .state(OpModeState.STOP)
            .onEnter(::mStop)
            .onEnter(CommandScheduler::resetScheduler)
            .onEnter(opModeTimer::reset)
            .transition { true }
            .build()

    override fun runOpMode() {
        mainStateMachine.reset()
        mainStateMachine.start()

        while(mainStateMachine.running) {
            mainStateMachine.update()
        }
    }


    open fun mInit() {}
    open fun mInitLoop() {}
    open fun mStart() {}
    open fun mLoop() {}
    open fun mStop() {}
    open fun mUniversal() {}
}