package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.hardware.Status
import robotuprising.lib.math.Angle
import robotuprising.lib.system.Subsystem

object DriveManager : Subsystem() {
    private val mainDrive = Homura
    private val rrDrive = VirtualDrive

    var imuAngle = Angle.EAST

    override fun init(hwMap: HardwareMap) {
        mainDrive.init(hwMap)
    }

    override fun update() {
        val drivePositions = mainDrive.wheelPositions
        rrDrive.externalHeading = imuAngle.angle
        rrDrive.setWheelPositions(drivePositions)

        val speeds = rrDrive.simulatedWheelPowers
        mainDrive.setDirectPowers(speeds)
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