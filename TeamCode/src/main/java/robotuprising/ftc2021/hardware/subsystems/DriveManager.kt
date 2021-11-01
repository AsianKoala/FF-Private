package robotuprising.ftc2021.hardware.subsystems

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.math.Angle
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.system.Subsystem

/**
 * Connects a virtual roadrunner drive to Ayame
 */
class DriveManager : Subsystem() {
    private val mainDrive = Ayame()
    private val rrDrive = VirtualDrive()

    var imuAngle = Angle.EAST

    var isAutomated = false

    private fun updateRRData() {
        rrDrive.externalHeading = imuAngle.angle
        rrDrive.setWheelPositions(mainDrive.wheelPositions)
        rrDrive.updatePoseEstimate()
    }

    fun setPowers(powers: Pose) {
        mainDrive.powers = powers
    }

    override fun init(hwMap: HardwareMap) {
        mainDrive.init(hwMap)
    }

    override fun update() {
        updateRRData()

        if (isAutomated) {
            mainDrive.rrPowers =  rrDrive.simulatedWheelPowers
        }

        mainDrive.update()
    }

    override fun stop() {
        mainDrive.stop()
    }

    override fun sendDashboardPacket() {
        mainDrive.sendDashboardPacket()
    }
}
