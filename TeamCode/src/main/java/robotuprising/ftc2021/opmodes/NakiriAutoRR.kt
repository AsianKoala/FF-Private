package robotuprising.ftc2021.opmodes

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.noahbres.meepmeep.core.toRadians
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.subsystems.vision.Webcam
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

@Autonomous(preselectTeleOp = "NakiriTeleOp")
class NakiriAutoRR : NakiriOpMode() {
    private val startX = 8.0
    private val startY = 63.0
    private val depositAngle = 45.0.radians
    private val depositY = 42.0

    private val initialDepositPose2d = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
        Pose2d(startX, depositY, depositAngle)
    } else {
        Pose2d(startX, -depositY, -depositAngle)
    }

    private val startPose = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
        Pose2d(startX, startY, 90.0.radians)
    } else {
        Pose2d(startX, -startY, (-90.0).radians)
    }


    private val moveToDepositTrajectorySequence = nakiri.ayame.trajectorySequenceBuilder(startPose)
            .lineToSplineHeading(initialDepositPose2d)
            .build()

    private val goToCraterTrajSequence = nakiri.ayame.trajectorySequenceBuilder(moveToDepositTrajectorySequence.end())
            .turn((-45.0).toRadians())
            .forward(24.0)
            .build()

    private enum class InitialDepositStates {
        MOVE_TO_DEPOSIT,
        DEPOSIT,
        CROSS_PIPES_INTO_CRATER
    }

    private val initialDepositStateMachine = StateMachineBuilder<InitialDepositStates>()
            .state(InitialDepositStates.MOVE_TO_DEPOSIT)
            .onEnter { nakiri.ayame.followTrajectorySequenceAsync(moveToDepositTrajectorySequence) }
            .transition { !nakiri.ayame.isBusy }
            .state(InitialDepositStates.DEPOSIT)
            .loop {
                when(nakiri.cupPosition) {
                    Webcam.CupStates.RIGHT -> nakiri.runAutoHighOuttake()
                    Webcam.CupStates.MIDDLE -> nakiri.runAutoMiddleOuttake()
                    Webcam.CupStates.LEFT -> nakiri.runAutoLowOuttake()
                }
            }
            .transition { !nakiri.outtaking }
            .state(InitialDepositStates.CROSS_PIPES_INTO_CRATER)
            .onEnter { nakiri.ayame.followTrajectorySequenceAsync(goToCraterTrajSequence) }
            .transition { !nakiri.ayame.isBusy }
            .build()

    override fun m_init_loop() {
        super.m_init_loop()
        nakiri.readWebcam()
    }

    override fun m_start() {
        super.start()
        nakiri.stopWebcam()
    }

    override fun m_loop() {
        super.m_loop()

        NakiriDashboard["alliance side"] = Globals.ALLIANCE_SIDE
        NakiriDashboard["cup state"] = nakiri.cupPosition
        NakiriDashboard["start point"] = Point(startX, startY)
    }

}