package org.firstinspires.ftc.teamcode.control.system

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import net.frogbots.ftcopmodetunercommon.opmode.TunableLinearOpMode
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl
import org.firstinspires.ftc.teamcode.hardware.Akemi
import org.firstinspires.ftc.teamcode.util.debug.Debuggable
import org.firstinspires.ftc.teamcode.util.math.Pose
import org.firstinspires.ftc.teamcode.util.opmode.AkemiTelemetry
import org.firstinspires.ftc.teamcode.util.opmode.Globals
import org.firstinspires.ftc.teamcode.util.opmode.OpModePacket
import org.firstinspires.ftc.teamcode.util.opmode.OpModeType
import org.firstinspires.ftc.teamcode.util.opmode.Status

abstract class BaseOpMode : TunableLinearOpMode() {
    abstract val startPose: Pose

    lateinit var akemi: Akemi

    lateinit var akemiTelemetry: AkemiTelemetry

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
        } else OpModeType.TELEOP

        akemiTelemetry = AkemiTelemetry(telemetry)

        val packet = OpModePacket(startPose, debugging, hardwareMap, akemiTelemetry, gamepad1, gamepad2)
        akemi = Akemi(packet)

        akemi.init()

        onInit()

        mainLoop@ while (true) {
            when (status) {
                Status.INIT_LOOP -> {
                    onInitLoop()
                }

                Status.LOOP -> {
                    if (hasStarted) {
                        onLoop()
                    } else {
                        onStart()
                        hasStarted = true
                    }
                }

                Status.STOP -> {
                    break@mainLoop
                }
            }
            akemiTelemetry.update()
        }

        onStop()
        if (opModeType == OpModeType.AUTO && Globals.IS_COMP)
            manager.initActiveOpMode(Globals.TELEOP_NAME)
    }

    open fun onInit() {}
    open fun onInitLoop() {}
    open fun onStart() {}
    abstract fun onLoop()
    open fun onStop() {}
}
