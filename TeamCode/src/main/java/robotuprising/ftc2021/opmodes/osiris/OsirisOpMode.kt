package robotuprising.ftc2021.opmodes.osiris

import robotuprising.ftc2021.manager.*
import robotuprising.ftc2021.subsystems.osiris.*
import robotuprising.ftc2021.subsystems.osiris.hardware.*
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.BlueWebcam
import robotuprising.ftc2021.subsystems.osiris.hardware.vision.RedWebcam
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.BaseOpMode

open class OsirisOpMode : BaseOpMode() {
//
    private val subsystemManager = SubsystemManager
    private val stateMachineManager = StateMachineManager
//
//    private val ghost = Ghost
//    private val odometry = Odometry
//
//    private val intake = Intake
//    private val loadingSensor = LoadingSensor
//
//    private val turretLimitSwitch = TurretLimitSwitch
//    private val slideLimitSwitch = SlideLimitSwitch
//
//    private val osiris = Osiris
    private val turret = Turret
//    private val slides = Slides
//    private val arm = Arm
//    private val outtake = Outtake
//    private val indexer = Indexer
//
//    private val spinner = Spinner
//
//    private val redWebcam = RedWebcam
//    private val blueWebcam = BlueWebcam

    open fun register() {
        subsystemManager.registerSubsystems(
//                ghost,
//                odometry,

//                intake,
//                loadingSensor,

//                outtake,
//                indexer,
//                arm,

//                slides

        turret

        )
    }

    override fun mInit() {
        subsystemManager.clearAll()

        register()
        subsystemManager.initAll()
    }

    override fun mInitLoop() {
        subsystemManager.periodic()
        stateMachineManager.periodic()
    }

    override fun mStart() {
//        ghost.driveState = Ghost.DriveStates.DISABLED
//        intake.disabled = false
//        slides.setSlideLockTarget(400.0)
        turret.setTurretLockAngle(45.0)
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