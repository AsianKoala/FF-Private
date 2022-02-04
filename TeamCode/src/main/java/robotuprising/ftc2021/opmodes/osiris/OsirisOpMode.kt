package robotuprising.ftc2021.opmodes.osiris

import robotuprising.ftc2021.manager.*
import robotuprising.ftc2021.subsystems.osiris.*
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.BlueWebcam
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.RedWebcam
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.BaseOpMode

open class OsirisOpMode : BaseOpMode() {

    private val subsystemManager = SubsystemManager
    private val stateMachineManager = StateMachineManager

    private val ghost = Ghost
    private val odometry = Odometry

    private val intake = Intake
    private val loadingSensor = LoadingSensor

    private val turret = Turret
    private val slides = Slides
    private val arm = Arm
    private val outtake = Outtake
    private val indexer = Indexer

//    private val turretLimitSwitch = TurretLimitSwitch
//    private val slideLimitSwitch = SlideLimitSwitch

//    private val spinner = Spinner
//
//    private val redWebcam = RedWebcam
//    private val blueWebcam = BlueWebcam

    open fun register() {
        subsystemManager.registerSubsystems(
                ghost,
//                odometry,

                intake,
                loadingSensor,
//                turretLimitSwitch,

                outtake,
                indexer,
                arm,

                slides,
//
                turret

        )
    }

    override fun mInit() {
        subsystemManager.clearAll()
        register()
        subsystemManager.initAll()

        intake.disabled = false
        turret.disabled = false
        slides.disabled = false
        turret.setTurretLockAngle(180.0)
        slides.setSlideInches(0.0)
    }

    override fun mInitLoop() {
        subsystemManager.periodic()
        subsystemManager.initServos()
        stateMachineManager.periodic()
    }

    override fun mStart() {
        ghost.driveState = Ghost.DriveStates.MANUAL
    }

    override fun mLoop() {
        subsystemManager.periodic()
        stateMachineManager.periodic()
    }

    override fun mStop() {
        subsystemManager.stopAll()
        stateMachineManager.stop()
    }
}