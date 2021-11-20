package robotuprising.ftc2021.hardware.subsystems

import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem

class Nakiri : Subsystem() {

    private val driveManager = DriveManager()
    private val intake = Intake()
    private val lift = Lift()
    private val linkage = Linkage()
    private val outtake = Outtake()
    private val duckSpinner = DuckSpinner()
    private val subsystems = mutableListOf(
        driveManager,
        intake,
        lift,
        linkage,
        outtake,
        duckSpinner,
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

    fun requestLiftLow() {
        lift.setLevel(Lift.LiftStages.LOW)
    }

    fun requestLiftHigh() {
        lift.setLevel(Lift.LiftStages.HIGH)
    }

    fun requestLinkageRetract() {
        linkage.retract()
    }

    fun requestLinkageOut() {
        linkage.extend()
    }

    fun requestLinkageMedium() {
        linkage.extendMed()
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

    fun requestSpinnerOn() {
        duckSpinner.turnOn()
    }

    fun requestSpinnerOff() {
        duckSpinner.turnOff()
    }
}
