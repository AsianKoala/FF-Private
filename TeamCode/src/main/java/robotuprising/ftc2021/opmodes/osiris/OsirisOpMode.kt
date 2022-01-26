package robotuprising.ftc2021.opmodes.osiris

import robotuprising.ftc2021.manager.*
import robotuprising.ftc2021.subsystems.osiris.*
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.BaseOpMode

open class OsirisOpMode : BaseOpMode() {

    protected val subsystemManager = SubsystemManager
    protected val dataManager = OsirisDataManager
    protected val gameStateManager = GameStateManager

    protected val ghost = Ghost
    protected val odometry = Odometry

    protected val intake = Intake
    protected val loadingSensor = LoadingSensor

    protected val turretLimitSwitch = TurretLimitSwitch
    protected val slideLimitSwitch = SlideLimitSwitch

    protected val osiris = Osiris
    protected val turret = Turret
    protected val slide = Slide
    protected val arm = Arm
    protected val outtake = Outtake
    protected val indexer = Indexer

    protected val spinner = Spinner

    protected val redWebcam = RedWebcam
    protected val blueWebcam = BlueWebcam


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

    // todo make a logger
    override fun mInit() {
        register()
        subsystemManager.prepareZero()
        gameStateManager.gameState = GameStates.IDLE
    }

    override fun mInitLoop() {
        subsystemManager.periodic()
    }

    override fun mStart() {
        subsystemManager.deregister(redWebcam)
        subsystemManager.deregister(blueWebcam)
        subsystemManager.deregister(turretLimitSwitch)

        gameStateManager.startTimer()
    }

    override fun mLoop() {
        subsystemManager.periodic()
    }

    override fun mStop() {
        subsystemManager.resetAll()
    }
}