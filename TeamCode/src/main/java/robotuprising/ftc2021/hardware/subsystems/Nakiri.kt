package robotuprising.ftc2021.hardware.subsystems

import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem

object Nakiri : Subsystem() {

    private val driveManager = DriveManager()
    private val intake = Intake()
    private val lift = Lift()
    private val linkage = Linkage()
    private val outtake = Outtake()
//    private val duckSpinner = DuckSpinner()
//    private val arm = MarkerGrabber()
    private val subsystems = mutableListOf(
            driveManager,
            intake,
            lift,
            outtake,
//            duckSpinner,
//            arm
    )

    override fun update() {
        subsystems.forEach { it.update() }
    }

    override fun sendDashboardPacket() {
        subsystems.forEach { it.sendDashboardPacket() }
    }

    override fun stop() {
        subsystems.forEach { it.stop() }
    }

    fun requestDriveManagerPowers(powers: Pose) {
        driveManager.vectorPowers = powers
    }

    fun requestDriveManagerStop() {
        driveManager.stop()
    }

    fun requestIntakeOn() {
        intake.turnOn()
    }

    fun requestIntakeOff() {
        intake.turnOff()
    }

    fun requestIntakeReverse() {
        intake.turnReverse()
    }

    fun requestIntakeRotateOut() {
        intake.rotateOut()
    }

    fun requestIntakeRotateIn() {
        intake.rotateIn()
    }

    fun requestLiftRest() {
        lift.setLevel(Lift.LiftStages.RESTING)
    }

    fun requestLiftMedium() {
        lift.setLevel(Lift.LiftStages.ALLIANCE_MEDIUM)
    }

    fun requestLiftHigh() {
        lift.setLevel(Lift.LiftStages.ALLIANCE_HIGH)
    }

    fun requestLinkageRetract() {
        linkage.retract()
    }

    fun requestLinkageOut() {
        linkage.extend()
    }

    fun requestOuttakeIn() {
        outtake.rotateIn()
    }

    fun requestOuttakeMedium() {
        outtake.rotateMedium()
    }

    fun requestOuttakeOut() {
        outtake.rotateOut()
    }


//    fun requestSpinnerOn() {
//        duckSpinner.turnOn()
//    }
//
//    fun requestSpinnerOff() {
//        duckSpinner.turnOff()
//    }
}
