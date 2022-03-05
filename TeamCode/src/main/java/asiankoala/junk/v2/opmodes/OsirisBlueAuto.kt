package asiankoala.junk.v2.opmodes

import asiankoala.junk.v2.auto.pp.PurePursuitPath
import asiankoala.junk.v2.lib.math.Angle
import asiankoala.junk.v2.lib.math.AngleUnit
import asiankoala.junk.v2.lib.math.Point
import asiankoala.junk.v2.lib.math.Pose
import asiankoala.junk.v2.lib.opmode.OsirisDashboard
import asiankoala.junk.v2.statemachines.JustDepositStateMachine
import asiankoala.junk.v2.subsystems.hardware.*
import asiankoala.junk.v2.subsystems.osiris.hardware.*
import asiankoala.koawalib.path.Waypoint
import asiankoala.koawalib.statemachine.StateMachineBuilder
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous
class OsirisBlueAuto : OsirisOpMode() {
    private val pathPoints = ArrayList<Waypoint>()
    private lateinit var path: PurePursuitPath

    override fun mInit() {
        super.mInit()
        Turret.zero()
        Turret.disabled = false
        Turret.setTurretLockAngle(240.0)
    }

    override fun mInitLoop() {
        super.mInitLoop()
        Indexer.lock()
        Outtake.cock()
    }

    override fun mStart() {
        super.mStart()
        Ghost.driveState = Ghost.DriveStates.MANUAL

        AutoStateMachine.stop()
        AutoStateMachine.reset()
        AutoStateMachine.start()
    }

    override fun mLoop() {
        super.mLoop()
        if (AutoStateMachine.running) {
            AutoStateMachine.update()
        }
    }

    override fun mStop() {
        super.mStop()
        AutoStateMachine.reset()
        AutoStateMachine.stop()
    }

    enum class AutoStates {
        DEPOSIT,
        RETRACT,
        PARK
    }

    enum class DepositHighStates {
        EXTEND,
        ARM
    }

    val DepositHighSM = StateMachineBuilder<DepositHighStates>()
        .state(DepositHighStates.EXTEND)
        .onEnter { Slides.setSlideInches(35.0) }
        .transitionTimed(0.5)
        .state(DepositHighStates.ARM)
        .onEnter {
            Outtake.depositHigh()
            Arm.depositHigh()
        }
        .transitionTimed(1.0)
        .build()

    val AutoStateMachine = StateMachineBuilder<AutoStates>()
        .state(AutoStates.DEPOSIT)
        .onEnter {
            DepositHighSM.stop()
            DepositHighSM.reset()
            DepositHighSM.start()
        }
        .loop {
            OsirisDashboard.addLine("LOOPING DEPOSIT SM")

            if (DepositHighSM.running) {
                DepositHighSM.update()
            }
        }
        .transitionTimed(2.0)
        .state(AutoStates.RETRACT)
        .onEnter(JustDepositStateMachine::start)
        .loop { OsirisDashboard.addLine("LOOPING RETRACT SM") }
        .transition(JustDepositStateMachine::done)
        .state(AutoStates.PARK)
        .onEnter { Ghost.powers = Pose(Point(-0.2, 1.0), Angle(0.0, AngleUnit.RAW)) }
        .onExit { Ghost.powers = Pose(AngleUnit.RAW) }
        .transitionTimed(0.8)
        .build()
}
