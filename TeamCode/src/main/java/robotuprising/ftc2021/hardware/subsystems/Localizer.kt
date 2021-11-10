package robotuprising.ftc2021.hardware.subsystems

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.rev.Rev2mDistanceSensor
import com.qualcomm.robotcore.hardware.HardwareMap
import robotuprising.lib.system.Subsystem
import robotuprising.lib.hardware.AxesSigns

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder

import robotuprising.lib.hardware.BNO055IMUUtil

import com.qualcomm.hardware.bosch.BNO055IMUImpl
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion

import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import robotuprising.lib.hardware.BNO055IMUUtil.remapAxes


class Localizer : Subsystem() {
    private lateinit var imu: BNO055IMU
    private lateinit var ySensor: Rev2mDistanceSensor
    private lateinit var xSensor: Rev2mDistanceSensor


    override fun init(hwMap: HardwareMap) {
        imu = hardwareMap.get(BNO055IMUImpl::class.java, "imu")
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS
        parameters.loggingEnabled = false
        imu.initialize(parameters)
        remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN)
    }

    override fun update() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun sendDashboardPacket() {
        TODO("Not yet implemented")
    }


}