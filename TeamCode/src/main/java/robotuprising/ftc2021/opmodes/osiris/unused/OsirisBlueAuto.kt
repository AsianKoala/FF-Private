package robotuprising.ftc2021.opmodes.osiris.unused

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import robotuprising.ftc2021.auto.pp.PurePursuitPath
import robotuprising.ftc2021.manager.SubsystemManager
import robotuprising.ftc2021.opmodes.osiris.OsirisOpMode
import robotuprising.ftc2021.statemachines.JustDepositStateMachine
import robotuprising.ftc2021.statemachines.AllianceReadyDepositStateMachine
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.BlueWebcam
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.Pipeline
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.RedWebcam
import robotuprising.ftc2021.util.Constants
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

    var cup_state = Pipeline.CupStates.LEFT

    override fun mInit() {
        super.mInit()
        Turret.zero()
    }

    override fun mInitLoop() {
        super.mInitLoop()
        Indexer.lock()
        Outtake.cock()
    }

    override fun mStart() {
        cup_state = BlueWebcam.pipeline.cupState
        super.mStart()
        Ghost.driveState = Ghost.DriveStates.MANUAL

        AutoStateMachine.stop()
        AutoStateMachine.reset()
        AutoStateMachine.start()
    }

    override fun mLoop() {
        super.mLoop()
        if(AutoStateMachine.running) {
            AutoStateMachine.update()
        }
    }

    override fun mStop() {
        super.mStop()
        AutoStateMachine.reset()
        AutoStateMachine.stop()
    }

    enum class AutoStates {
        TURRET,
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
            .transitionTimed(2.0)

            .build()

    enum class DepositMediumStates {
        EXTEND,
        ARM
    }

    val DepositMedSM = StateMachineBuilder<DepositMediumStates>()
            .state(DepositMediumStates.EXTEND)
            .onEnter {
                Slides.maxCap = 0.5
                Slides.setSlideInches(27.5)
            }
            .transition { true }

            .state(DepositMediumStates.ARM)
            .onEnter {
                Outtake.moveServoToPosition(1.0)
                Arm.moveServoToPosition(Constants.armMediumPosition)
            }
            .transitionTimed(2.0)


            .build()



    val AutoStateMachine = StateMachineBuilder<AutoStates>()
            .state(AutoStates.TURRET)
            .onEnter { Turret.setTurretLockAngle(237.0) }
            .transitionTimed(1.0)

            .state(AutoStates.DEPOSIT)
            .onEnter {
                when(cup_state) {
                    Pipeline.CupStates.LEFT -> {
                        DepositHighSM.stop()
                        DepositHighSM.reset()
                        DepositHighSM.start()
                    }

                    Pipeline.CupStates.CENTER -> {
                        DepositMedSM.stop()
                        DepositMedSM.reset()
                        DepositMedSM.start()
                    }

                    Pipeline.CupStates.RIGHT -> {
                        DepositHighSM.stop()
                        DepositHighSM.reset()
                        DepositHighSM.start()
                    }

                }
            }

            .loop {
                OsirisDashboard.addLine("LOOPING DEPOSIT SM")

                if(DepositMedSM.running) {
                    DepositMedSM.update()
                }

                if(DepositHighSM.running) {
                    DepositHighSM.update()
                }
            }

            .onExit {
                Slides.maxCap = 1.0
            }
            .transitionTimed(2.0)

            .state(AutoStates.RETRACT)
            .onEnter(JustDepositStateMachine::start)
            .loop { OsirisDashboard.addLine("LOOPING RETRACT SM") }
            .transition(JustDepositStateMachine::done)

            .state(AutoStates.PARK)
//            .onEnter { Ghost.powers = Pose(Point(0.0, 0.7), Angle(0.0, AngleUnit.RAW)) }
//            .onExit { Ghost.powers = Pose(AngleUnit.RAW) }
            .transitionTimed(0.5)

            .build()
}