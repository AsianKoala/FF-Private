package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.subsystem.odometry.KEncoder
import com.asiankoala.koawalib.subsystem.odometry.KThreeWheelOdometry

class Encoders(hardware: Hardware) {
    private val ticksPerUnit = 1892.3724

    val turretEncoder = KEncoder(hardware.turretMotor, 5.33333).zero(Turret.turretHomeValue)
    val slideEncoder = KEncoder(hardware.slideMotor, 20.8333).zero().reversed
    private val leftEncoder = KEncoder(hardware.frMotor, ticksPerUnit, true).zero()
    private val rightEncoder = KEncoder(hardware.flMotor, ticksPerUnit, true).zero()
    private val auxEncoder = KEncoder(hardware.brMotor, ticksPerUnit, true).zero()

    private val trackWidth = 8.36 // 8.690685
    private val perpTracker = 6.4573 // 7.641969
    val odo = KThreeWheelOdometry(
            leftEncoder,
            rightEncoder,
            auxEncoder,
            trackWidth,
            perpTracker
    )
}