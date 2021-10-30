package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.hardware.MecanumPowers
import robotuprising.lib.hardware.Status
import robotuprising.lib.math.Angle
import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem

object DriveManager : Subsystem() {
    private val mainDrive = Ayame
    private val rrDrive = VirtualDrive

    var imuAngle = Angle.EAST

    var isAutomated = false

    private fun updateRRData() {
        rrDrive.externalHeading = imuAngle.angle
        rrDrive.setWheelPositions(mainDrive.wheelPositions)
        rrDrive.updatePoseEstimate()
    }

    fun setHomuraVectors(powers: MecanumPowers) {
        mainDrive.setFromMecanumPowers(powers)
    }

    fun setRRPoseEstimate(pose: Pose) {
        rrDrive.poseEstimate = Pose2d(pose.x, pose.y, pose.h.angle)
    }

    override fun init(hwMap: HardwareMap) {
        mainDrive.init(hwMap)
    }

    override fun update() {
        updateRRData()

        if(isAutomated) {
            mainDrive.setDirectPowers(rrDrive.simulatedWheelPowers)
        }

        mainDrive.update()
        status = mainDrive.status
    }

    override fun stop() {
        mainDrive.stop()
    }

    override fun sendDashboardPacket() {
        mainDrive.sendDashboardPacket()
    }

    override var status: Status = Status.ALIVE
}