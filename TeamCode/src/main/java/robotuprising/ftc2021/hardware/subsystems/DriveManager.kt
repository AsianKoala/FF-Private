package robotuprising.ftc2021.hardware.subsystems

import robotuprising.lib.math.Angle
import robotuprising.lib.math.AngleUnit
import robotuprising.lib.math.Point
import robotuprising.lib.math.Pose
import robotuprising.lib.opmode.NakiriDashboard
import robotuprising.lib.system.Subsystem

/**
 * Connects a virtual roadrunner drive to Ayame
 */
// TODO
// TODO LITERALLY JUST FUCKING CREATE A CLASS FOR UPDATING DT MOTOR POWERS CAUSE THIS IS PAINFUL AF PAIN PAIN PAIN
class DriveManager() : Subsystem() {
    private val mainDrive = Ayame()
//    private val rrDrive = VirtualDrive()
//
//    var imuAngle = Angle.EAST
//
//    var isDriveAutomated = false
//
//    private var internalWheelPowers: List<Double> = mutableListOf()
//
//    var vectorPowers: Pose = Pose(Point.ORIGIN, Angle(0.0, AngleUnit.RAW))
//        set(value) {
//            internalWheelPowers = Ayame.convertVectorPowersToWheels(value)
//            field = value
//        }

    private var internalWheelPowers = mutableListOf(0.0, 0.0, 0.0, 0.0)
    var vectorPowers: Pose = Pose(Point.ORIGIN, Angle(0.0, AngleUnit.RAW))
        set(value) {
            internalWheelPowers = Ayame.convertVectorPowersToWheels(value)
            field = value
        }

    override fun update() {
//        if(isDriveAutomated) {
//            internalWheelPowers = rrDrive.simulatedWheelPowers
//        }
//
//        rrDrive.externalHeading = imuAngle.angle
//        rrDrive.setWheelPositions(mainDrive.wheelPositions)
//        rrDrive.updatePoseEstimate()
//
//        mainDrive.wheels = internalWheelPowers
//        mainDrive.update()

        mainDrive.wheels = internalWheelPowers
        mainDrive.update()
    }

    override fun stop() {
        mainDrive.stop()
    }

    override fun sendDashboardPacket() {
        NakiriDashboard.name = "drive manager"
        NakiriDashboard["vector powers"] = vectorPowers
        mainDrive.sendDashboardPacket()
    }
}
