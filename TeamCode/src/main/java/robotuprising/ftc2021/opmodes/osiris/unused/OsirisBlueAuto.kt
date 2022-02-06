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

    enum class DepositMediumStates {
        EXTEND,
        ARM
    }

    val DepositMedSM = StateMachineBuilder<DepositMediumStates>()
            .state(DepositMediumStates.EXTEND)
            .onEnter { Slides.setSlideInches(30.0) }
            .transitionTimed(0.5)

            .state(DepositMediumStates.ARM)
            .onEnter {
                Outtake.depositHigh()
                Arm.moveServoToPosition(Constants.armMediumPosition)
            }
            .transitionTimed(2.0)


            .build()

    enum class DepositLowStates {
        EXTEND,
        ARM
    }

    val DepositLowSM = StateMachineBuilder<DepositLowStates>()
            .state(DepositLowStates.EXTEND)
            .onEnter { Slides.setSlideInches(28.0) }
            .transitionTimed(0.5)

            .state(DepositLowStates.ARM)
            .onEnter {
                Outtake.depositHigh()
                Arm.moveServoToPosition(Constants.armLowPosition)
            }
            .transitionTimed(2.0)
            .build()





    val AutoStateMachine = StateMachineBuilder<AutoStates>()
            .state(AutoStates.TURRET)
            .onEnter { Turret.setTurretLockAngle(245.0) }
            .transitionTimed(2.0)

            .state(AutoStates.DEPOSIT)
            .onEnter {
                when(cup_state) {
                    Pipeline.CupStates.LEFT -> {
                        DepositLowSM.stop()
                        DepositLowSM.reset()
                        DepositLowSM.start()
                    }

                    Pipeline.CupStates.CENTER -> {
                        DepositMedSM.stop()
                        DepositMedSM.reset()
                        DepositMedSM.start()
                    }

                    Pipeline.CupStates.RIGHT -> AllianceReadyDepositStateMachine.start()

                }
            }

            .loop {
                if(DepositLowSM.running) {
                    DepositLowSM.update()
                }

                if(DepositMedSM.running) {
                    DepositMedSM.update()
                }
            }

            .transition {
                when(cup_state) {
                    Pipeline.CupStates.LEFT -> DepositLowSM.running
                    Pipeline.CupStates.CENTER -> DepositMedSM.running
                    Pipeline.CupStates.RIGHT -> AllianceReadyDepositStateMachine.done
                }
            }

            .state(AutoStates.RETRACT)
            .onEnter(JustDepositStateMachine::start)
            .transition(JustDepositStateMachine::done)

            .state(AutoStates.PARK)
//            .onEnter { Ghost.powers = Pose(Point(0.0, 0.7), Angle(0.0, AngleUnit.RAW)) }
//            .onExit { Ghost.powers = Pose(AngleUnit.RAW) }
            .transitionTimed(0.5)

            .build()
}