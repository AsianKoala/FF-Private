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

    private val ghost = Ghost
    private val odometry = Odometry

    private val intake = Intake
    private val loadingSensor = LoadingSensor

    private val turretLimitSwitch = TurretLimitSwitch
    private val slideLimitSwitch = SlideLimitSwitch

    private val osiris = Osiris
    private val turret = Turret
    private val slide = Slide
    private val arm = Arm
    private val outtake = Outtake
    private val indexer = Indexer

    private val spinner = Spinner

    private val redWebcam = RedWebcam
    private val blueWebcam = BlueWebcam

    open fun register() {
        subsystemManager.registerSubsystems(
                ghost,
                odometry,

                intake,
                loadingSensor,

                turretLimitSwitch,
                slideLimitSwitch,

                osiris,
                turret,
                slide,
                arm,
                outtake,
                indexer,

                spinner,

                redWebcam,
                blueWebcam
        )

        if(allianceSide == AllianceSide.BLUE) {
            subsystemManager.deregister(redWebcam)
        }

        if(allianceSide == AllianceSide.RED) {
            subsystemManager.deregister(blueWebcam)
        }
    }

    override fun mInit() {
        register()
        subsystemManager.initAll()

        ghost.driveState = Ghost.DriveStates.DISABLED
    }

    override fun mInitLoop() {
        subsystemManager.periodic()
    }

    override fun mStart() {
        subsystemManager.deregister(redWebcam)
        subsystemManager.deregister(blueWebcam)
    }

    override fun mLoop() {
        subsystemManager.periodic()
    }

    override fun mStop() {
        subsystemManager.stopAll()
    }
}