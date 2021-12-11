package robotuprising.ftc2021.opmodes

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.subsystems.vision.Webcam
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.control.auto.path.PurePursuitController
import robotuprising.lib.control.auto.waypoints.LockedWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.*
import robotuprising.lib.math.MathUtil.degrees
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder
import kotlin.math.absoluteValue

@Autonomous(preselectTeleOp = "NakiriTeleOp")
class NakiriAuto : NakiriOpMode() {

    private val startX = 8.0
    private val startY = 63.0
    private val depositAngle = 45.0
    private val depositY = 42.0
    private val initialDepositWaypoint = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
        LockedWaypoint(startX, depositY, 0.0, Angle(depositAngle.radians, AngleUnit.RAD))
    } else {
        LockedWaypoint(startX, -depositY, 0.0, Angle(-depositAngle.radians, AngleUnit.RAD))
    }

    private enum class InitialDepositStates {
        MOVE_TO_DEPOSIT,
        DEPOSIT,
        TURN,
        CROSS_PIPES_INTO_CRATER
    }

    private val initialDepositStateMachine = StateMachineBuilder<InitialDepositStates>()
            .state(InitialDepositStates.MOVE_TO_DEPOSIT)
            .loop { nakiri.requestAyamePowers(PurePursuitController.curve(nakiri.currPose, initialDepositWaypoint))}
            .transition { MathUtil.waypointThresh(nakiri.currPose, initialDepositWaypoint, 1.0) }

            .state(InitialDepositStates.DEPOSIT)
            .loop {
                when(nakiri.cupPosition) {
                    Webcam.CupStates.RIGHT -> nakiri.runAutoHighOuttake()
                    Webcam.CupStates.MIDDLE -> nakiri.runAutoMiddleOuttake()
                    Webcam.CupStates.LEFT -> nakiri.runAutoLowOuttake()
                }
            }
            .transition { !nakiri.outtaking }

            .state(InitialDepositStates.TURN)
            .loop { nakiri.requestAyamePowers(PurePursuitController.turn(nakiri.currPose, Angle(0.0, AngleUnit.RAD))) }
            .transition { nakiri.currPose.h.deg.absoluteValue < 1.0 }

            .state(InitialDepositStates.CROSS_PIPES_INTO_CRATER)
            .loop { nakiri.requestAyamePowers(0.0, 1.0, 0.0) }
            .transitionTimed(1.5)
            .build()


    override fun m_init() {
        super.m_init()
        nakiri.startWebcam()

        nakiri.setStartingPose(
                if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
                    Pose(Point(startX, startY), Angle(90.0.radians, AngleUnit.RAD))
                } else {
                    Pose(Point(startX, -startY), Angle((-90.0).radians, AngleUnit.RAD))
                }
        )
    }

    override fun m_init_loop() {
        super.m_init_loop()
        nakiri.readWebcam()
    }

    override fun m_start() {
        super.start()
        nakiri.stopWebcam()
        initialDepositStateMachine.reset()
        initialDepositStateMachine.start()
    }

    override fun m_loop() {
        super.m_loop()
        initialDepositStateMachine.update()

        if(!initialDepositStateMachine.running) {
            requestOpModeStop()
        }

        NakiriDashboard["alliance side"] = Globals.ALLIANCE_SIDE
        NakiriDashboard["cup state"] = nakiri.cupPosition
        NakiriDashboard["start point"] = Point(startX, startY)
    }
}