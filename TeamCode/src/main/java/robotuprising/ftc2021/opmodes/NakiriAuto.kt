package robotuprising.ftc2021.opmodes

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.subsystems.vision.Webcam
import robotuprising.lib.control.auto.path.PurePursuitController
import robotuprising.lib.control.auto.waypoints.LockedWaypoint
import robotuprising.lib.control.auto.waypoints.Waypoint
import robotuprising.lib.math.*
import robotuprising.lib.math.MathUtil.degrees
import robotuprising.lib.system.statemachine.StateMachineBuilder
import kotlin.math.absoluteValue

@Config
@Autonomous(preselectTeleOp = "NakiriTeleOp")
class NakiriAuto : NakiriOpMode() {

    private val initialDepositWaypoint = LockedWaypoint(7.0, 42.0, 0.0, Angle(45.0.degrees, AngleUnit.RAD))

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
    }
}



/*

    private val intakeWaypoint = LockedWaypoint(0.0, 0.0, 0.0, Angle.EAST)
    private val craterCrossWaypoint = LockedWaypoint(0.0, 0.0, 0.0, Angle.EAST)
    private val hubPoint = Point.ORIGIN

    private var startTime = 0L
    private var turning = false




      private enum class CyclingStates {
        GO_TO_CRATER,
        INTAKE,
        MOVE_TO_CROSS_CRATER,
        CROSS,
        DEPOSIT,
        RESET
    }

    private val cycleStateMachine = StateMachineBuilder<CyclingStates>()
            .state(CyclingStates.GO_TO_CRATER)
            .onEnter { nakiri.startGoingOverPipes() }
            .loop { nakiri.requestAyamePowers(0.0, 1.0, 0.0) }
            .transition { nakiri.inCrater }
            .onExit { nakiri.requestAyameStop() }

            .state(CyclingStates.INTAKE)
            .onEnter { nakiri.runIntakeSequence(true) }
            .loop {
                nakiri.runIntakeSequence(false)
                val output = PurePursuitController.curve(nakiri.currPose, intakeWaypoint, 1.0, 90.0)
                nakiri.requestAyamePowers(output)
            }
            .transition { !nakiri.intaking }
            .onExit { nakiri.requestAyameStop() }

            .state(CyclingStates.MOVE_TO_CROSS_CRATER)
            .loop {
                val output = PurePursuitController.curve(nakiri.currPose, craterCrossWaypoint)
                nakiri.requestAyamePowers(output)
            }
            .transition { (nakiri.currPose.p - craterCrossWaypoint.p).hypot < 2.0 }

            .state(CyclingStates.CROSS)
            .onEnter { nakiri.startGoingOverPipes() }
            .loop { nakiri.requestAyamePowers(0.0, -1.0, 0.0) }
            .transition { nakiri.inField }

            .state(CyclingStates.DEPOSIT)
            .onEnter { nakiri.runAutoOuttake(true) }
            .loop {
                val deltaPos = hubPoint - nakiri.currPose.p
                val dh = deltaPos.atan2

                val output = PurePursuitController.curve(nakiri.currPose, LockedWaypoint(
                        hubPoint.x,
                        hubPoint.y,
                        0.0,
                        dh
                ))

                nakiri.requestAyamePowers(0.0, 0.0, output.h.angle)

                nakiri.runAutoOuttake(false)
            }
            .transition { !nakiri.outtaking }
            .onExit { nakiri.requestAyameStop() }

            .state(CyclingStates.RESET)
            .loop {
                val output = PurePursuitController.curve(nakiri.currPose, LockedWaypoint(
                        hubPoint.x,
                        hubPoint.y,
                        0.0,
                        Angle.EAST
                ))

                nakiri.requestAyamePowers(0.0, 0.0, output.h.angle)
            }
            .transition { nakiri.currPose.h.angle.degrees.absoluteValue < 2.0 }
            .onExit { nakiri.requestAyameStop() }
            .build()


 //            .state(AutoStates.CYCLING)
//            .onEnter {
//                cycleStateMachine.reset()
//                cycleStateMachine.start()
//            }
//            .loop {
//                if(cycleStateMachine.running) {
//                    cycleStateMachine.update()
//                } else {
//                    cycleStateMachine.reset()
//                    cycleStateMachine.start()
//                }
//            }
//            .transition { System.currentTimeMillis() - startTime > 2700 }
//
//            .state(AutoStates.PARKING)
//            .onEnter {
//                nakiri.requestAyameStop()
//
//                if(nakiri.inField && nakiri.currPose.h.angle.degrees.absoluteValue > 2.0) {
//                    turning = true
//                } else if(nakiri.inField) {
//                    nakiri.startGoingOverPipes()
//                }
//            }
//            .loop {
//                if(turning) {
//                    val output = PurePursuitController.curve(nakiri.currPose, fieldCrossWaypoint)
//                    nakiri.requestAyamePowers(0.0, 0.0, output.h.angle)
//                } else {
//                    nakiri.requestAyamePowers(0.0, 1.0, 0.0)
//                }
//            }
//            .transition { nakiri.inCrater }
//            .onExit { nakiri.reset() }

 */