package robotuprising.ftc2021.opmodes

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.lib.control.auto.path.PurePursuitController
import robotuprising.lib.control.auto.waypoints.LockedWaypoint
import robotuprising.lib.math.Angle
import robotuprising.lib.math.MathUtil.degrees
import robotuprising.lib.math.Point
import robotuprising.lib.system.statemachine.StateMachineBuilder
import kotlin.math.absoluteValue

@Config
@Autonomous
class NakiriAuto : NakiriOpMode() {

    companion object {
        @JvmStatic var intakeWaypoint = LockedWaypoint(0.0, 0.0, 0.0, Angle.EAST)
        @JvmStatic var craterCrossWaypoint = LockedWaypoint(0.0, 0.0, 0.0, Angle.EAST)
        @JvmStatic var fieldCrossWaypoint = LockedWaypoint(0.0, 0.0, 0.0, Angle.EAST)
        @JvmStatic var hubPoint = Point.ORIGIN
    }

    private var startTime = 0L
    private var turning = false

    private enum class AutoStates {
        INITIAL_DEPOSIT,
        CYCLING,
        PARKING
    }

    private enum class InitialDepositStates {
        MOVE_TO_DEPOSIT,
        DEPOSIT,
        MOVE_TO_CROSS_FIELD,
    }

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

    private val initialDepositStateMachine = StateMachineBuilder<InitialDepositStates>()
            .state(InitialDepositStates.MOVE_TO_DEPOSIT)
            .state(InitialDepositStates.DEPOSIT)
            .onEnter { nakiri.runAutoOuttake(true) }
            .loop { nakiri.runAutoOuttake(false) }
            .transition { !nakiri.outtaking }
            .state(InitialDepositStates.MOVE_TO_CROSS_FIELD)
            .loop {
                val output = PurePursuitController.curve(nakiri.currPose, fieldCrossWaypoint)
                nakiri.requestAyamePowers(output)
            }
            .transition { (nakiri.currPose.p - fieldCrossWaypoint.p).hypot < 2.0 }
            .build()

    private val autoStateMachine = StateMachineBuilder<AutoStates>()
            .state(AutoStates.INITIAL_DEPOSIT)
            .onEnter {
                initialDepositStateMachine.reset()
                initialDepositStateMachine.start()
            }
            .loop { initialDepositStateMachine.update() }
            .transition { !initialDepositStateMachine.running }

            .state(AutoStates.CYCLING)
            .onEnter {
                cycleStateMachine.reset()
                cycleStateMachine.start()
            }
            .loop {
                if(cycleStateMachine.running) {
                    cycleStateMachine.update()
                } else {
                    cycleStateMachine.reset()
                    cycleStateMachine.start()
                }
            }
            .transition { System.currentTimeMillis() - startTime > 2700 }

            .state(AutoStates.PARKING)
            .onEnter {
                nakiri.requestAyameStop()

                if(nakiri.inField && nakiri.currPose.h.angle.degrees.absoluteValue > 2.0) {
                    turning = true
                } else if(nakiri.inField) {
                    nakiri.startGoingOverPipes()
                }
            }
            .loop {
                if(turning) {
                    val output = PurePursuitController.curve(nakiri.currPose, fieldCrossWaypoint)
                    nakiri.requestAyamePowers(0.0, 0.0, output.h.angle)
                } else {
                    nakiri.requestAyamePowers(0.0, 1.0, 0.0)
                }
            }
            .transition { nakiri.inCrater }
            .onExit { nakiri.reset() }
            .build()

    override fun m_start() {
        super.start()
        startTime = System.currentTimeMillis()
        autoStateMachine.reset()
        autoStateMachine.start()
    }

    override fun m_loop() {
        super.m_loop()
        autoStateMachine.update()
    }
}