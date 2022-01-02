package robotuprising.ftc2021.opmodes.wraith

import robotuprising.ftc2021.manager.*
import robotuprising.ftc2021.subsystems.wraith.*
import robotuprising.lib.opmode.AllianceSide
import robotuprising.lib.system.BaseOpMode

open class Robot : BaseOpMode() {

    private val subsystemManager = SubsystemManager
    private val dataManager = WraithDataManager
    private val gameStateManager = GameStateManager

    private val ghost = Ghost
    private val odometry = Odometry

    private val intake = Intake
    private val intakeSensor = IntakeSensor
    private val loadingSensor = LoadingSensor

    private val turretLimitSwitch = TurretLimitSwitch
    private val slideLimitSwitch = SlideLimitSwitch

    private val wraith = Wraith
    private val turret = Turret
    private val slide = Slide
    private val arm = Arm
    private val outtake = Outtake
    private val indexer = Indexer

    private val spinner = Spinner
//    private val capstone = Capstone

    private val redWebcam = RedWebcam
    private val blueWebcam = BlueWebcam


    // todo make a logger
    override fun mInit() {
        subsystemManager.registerSubsystems(
                ghost,
                odometry,

                intake,
                intakeSensor,
                loadingSensor,

                turretLimitSwitch,
                slideLimitSwitch,

                wraith,
                turret,
                slide,
                arm,
                outtake,
                indexer,

                spinner,
//                capstone,
                redWebcam,
                blueWebcam
        )

        if(allianceSide == AllianceSide.BLUE) {
            subsystemManager.deregister(redWebcam)
        }

        if(allianceSide == AllianceSide.RED) {
            subsystemManager.deregister(blueWebcam)
        }


        turretLimitSwitch.link(turret)
        slideLimitSwitch.link(slide)


        subsystemManager.resetAll()

        gameStateManager.systemState = SystemStates.INIT
        gameStateManager.gameState = GameStates.IDLE
    }

    override fun mInitLoop() {
        subsystemManager.readAll()

        subsystemManager.loopAll()
    }

    override fun mLoop() {
        subsystemManager.readAll()

        subsystemManager.loopAll()
    }

    override fun mStop() {
        subsystemManager.resetAll()
    }

    override fun mTest() {
        subsystemManager.testAll()
    }
}