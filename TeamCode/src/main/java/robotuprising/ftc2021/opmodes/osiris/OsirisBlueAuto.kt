package robotuprising.ftc2021.opmodes.osiris

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.auto.pp.PurePursuitPath
import robotuprising.ftc2021.auto.pp.Subroutines
import robotuprising.ftc2021.manager.SubsystemManager
import robotuprising.ftc2021.statemachines.AutoAimBlueStateMachine
import robotuprising.ftc2021.statemachines.IntakeStateMachine
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.ReadyForDepositStateMachine
import robotuprising.ftc2021.subsystems.osiris.hardware.Ghost
import robotuprising.ftc2021.subsystems.osiris.hardware.Odometry
import robotuprising.ftc2021.subsystems.osiris.hardware.Slides
import robotuprising.ftc2021.subsystems.osiris.hardware.Turret
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.BlueWebcam
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.RedWebcam
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.OsirisDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder
import robotuprising.lib.util.Extensions.varargAdd

@Autonomous
class OsirisBlueAuto : OsirisOpMode() {
    private val pathPoints = ArrayList<Waypoint>()
    private lateinit var path: PurePursuitPath

    override fun mInit() {
        super.mInit()

        Odometry.startPose = Pose(Point(8.0, 64.0), Angle(0.0, AngleUnit.RAD))

        path = PurePursuitPath(pathPoints)
        Ghost.currentPath = path
        Ghost.driveState = Ghost.DriveStates.PATH

        Turret.zero()
        Turret.setTurretLockAngle(245.0)
        Slides.setSlideInches(0.0)
    }

    override fun mLoop() {
        super.mLoop()
        if(Ghost.driveState == Ghost.DriveStates.DISABLED) stop()
    }

    override fun mStart() {
        SubsystemManager.deregister(RedWebcam)
        SubsystemManager.deregister(BlueWebcam)

        AutoAimBlueStateMachine.startForTeleop()
    }

    private enum class FDATLSMStates {
        READY_FOR_DEPOSIT_RUNNING,
        DEPOSIT_RUNNING
    }

    private val fullDepositAfterTurretLockedStateMachine = StateMachineBuilder<FDATLSMStates>()
            .state(FDATLSMStates.READY_FOR_DEPOSIT_RUNNING)
            .onEnter(ReadyForDepositStateMachine::start)
            .loop { OsirisDashboard.addLine("RFDSM AUTO RUNNING") }
            .transition(ReadyForDepositStateMachine::done)

            .state(FDATLSMStates.DEPOSIT_RUNNING)
            .onEnter(JustDepositStateMachine::start)
            .loop { OsirisDashboard.addLine("DEPOSIT AUTO RUNNING") }
            .transition(JustDepositStateMachine::done)
            .build()

    private fun bruh(): Boolean {
        if(!fullDepositAfterTurretLockedStateMachine.running) {
            fullDepositAfterTurretLockedStateMachine.reset()
            fullDepositAfterTurretLockedStateMachine.start()
        }

        fullDepositAfterTurretLockedStateMachine.update()

        return !fullDepositAfterTurretLockedStateMachine.running
    }

   init {
       pathPoints.varargAdd(
               Waypoint(8, 64, 0),
               Waypoint(8, 64, 0, Subroutines.ArrivalInterruptSubroutine(this::bruh)),
               Waypoint(24, 64, 8, Subroutines.OnceOffSubroutine(IntakeStateMachine::start)),
               Waypoint(64, 64, 8, Subroutines.RepeatedSubroutine(IntakeStateMachine::hasIntaked)),
               Waypoint(24, 64, 8, Subroutines.ArrivalInterruptSubroutine(this::bruh))
       )
   }
}