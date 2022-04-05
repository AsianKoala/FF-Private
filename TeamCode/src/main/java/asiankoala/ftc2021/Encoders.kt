package asiankoala.ftc2021

import asiankoala.ftc2021.subsystems.Turret
import com.asiankoala.koawalib.subsystem.odometry.KEncoder

class Encoders(hardware: Hardware) {
    private val ticksPerUnit = 1892.3724

    val turretEncoder = KEncoder(hardware.turretMotor, 5.33333).zero(Turret.turretHomeValue)
    val slideEncoder = KEncoder(hardware.slideMotor, 20.8333).reversed.zero()
    val leftEncoder = KEncoder(hardware.frMotor, ticksPerUnit, true).zero()
    val rightEncoder = KEncoder(hardware.flMotor, ticksPerUnit, true).zero()
    val auxEncoder = KEncoder(hardware.brMotor, ticksPerUnit, true).zero()
}