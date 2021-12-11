package robotuprising.ftc2021.opmodes

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.noahbres.meepmeep.core.toRadians
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.auto.trajectorysequence.TrajectorySequence
import robotuprising.ftc2021.subsystems.vision.Webcam
import robotuprising.ftc2021.util.Globals
import robotuprising.lib.math.MathUtil.radians
import robotuprising.lib.math.Point
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.statemachine.StateMachineBuilder

@Autonomous(preselectTeleOp = "NakiriTeleOp")
class NakiriAuto : NakiriOpMode() {
    private val startX = 10.0
    private val startY = 60.0
    private val depositAngle = 42.0.radians
    private val depositX = 5.0
    private val depositY = 41.5


    private val initialDepositPose2d = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
        Pose2d(depositX, depositY, depositAngle)
    } else {
        Pose2d(depositX, -depositY, -depositAngle)
    }

    private val startPose = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
        Pose2d(startX, startY, 90.0.radians)
    } else {
        Pose2d(startX, -startY, (-90.0).radians)
    }

    private lateinit var moveToDepositTrajectorySequence: TrajectorySequence

    private lateinit var goToCraterTrajSequence: TrajectorySequence

    private enum class InitialDepositStates {
        MOVE_TO_DEPOSIT,
        DEPOSIT,
        TURN,
        CROSS
    }

    private val initialDepositStateMachine = StateMachineBuilder<InitialDepositStates>()
            .state(InitialDepositStates.MOVE_TO_DEPOSIT)
            .onEnter { nakiri.ayame.followTrajectorySequenceAsync(moveToDepositTrajectorySequence) }
            .transition { !nakiri.ayame.isBusy }
            .state(InitialDepositStates.DEPOSIT)
            .loop {
                when(Globals.CUP_LOCATION) {
                    Webcam.CupStates.RIGHT -> nakiri.runAutoHighOuttake()
                    Webcam.CupStates.MIDDLE -> nakiri.runAutoMiddleOuttake()
                    Webcam.CupStates.LEFT -> nakiri.runAutoLowOuttake()
                }
            }
            .transition { !nakiri.outtaking }
            .state(InitialDepositStates.TURN)
            .onEnter { nakiri.ayame.followTrajectorySequenceAsync(goToCraterTrajSequence) }
            .transition { !nakiri.ayame.isBusy }

            .state(InitialDepositStates.CROSS)
            .loop { nakiri.ayame.setVectorPowers(0.0, 0.85, 0.0) }
            .onExit { nakiri.ayame.setVectorPowers(0.0, 0.0, 0.0) }
            .transitionTimed(1.3)
            .build()

    override fun m_init() {
        super.m_init()

        moveToDepositTrajectorySequence = nakiri.ayame.trajectorySequenceBuilder(startPose)
                .lineToSplineHeading(initialDepositPose2d)
                .build()


        goToCraterTrajSequence = if(Globals.ALLIANCE_SIDE == AllianceSide.BLUE) {
            nakiri.ayame.trajectorySequenceBuilder(moveToDepositTrajectorySequence.end())
                    .turn(-depositAngle)
                    .strafeLeft(6.0)
                    .build()
        } else {
            nakiri.ayame.trajectorySequenceBuilder(moveToDepositTrajectorySequence.end())
                    .turn(depositAngle)
                    .strafeRight(6.3)
                    .build()
        }

        nakiri.ayame.poseEstimate = startPose
        nakiri.startWebcam()
    }

    override fun m_init_loop() {
        super.m_init_loop()
        nakiri.updateWebcam()
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
    }

}