package com.asiankoala.koawalib.subsystem.drive

import com.asiankoala.koawalib.hardware.motor.KMotor
import com.asiankoala.koawalib.math.Pose
import com.asiankoala.koawalib.subsystem.DeviceSubsystem
import com.asiankoala.koawalib.util.Logger
import kotlin.math.absoluteValue

open class KMecanumDrive(
    fl: KMotor,
    bl: KMotor,
    br: KMotor,
    fr: KMotor
) : DeviceSubsystem() {
    private val motors = listOf(fl, bl, br, fr)

    var powers = Pose()

    protected open fun processPowers(drivePowers: Pose): List<Double> {
        val fl = drivePowers.y + drivePowers.x - drivePowers.heading
        val bl = drivePowers.y - drivePowers.x - drivePowers.heading
        val br = drivePowers.y + drivePowers.x + drivePowers.heading
        val fr = drivePowers.y - drivePowers.x + drivePowers.heading
        val ret = listOf(fl, bl, br, fr)
        Logger.logInfo(ret.toString())
        return ret
    }

    override fun periodic() {
        val wheels = processPowers(powers)
        val absMax = wheels.maxOf { it.absoluteValue }
        val scalar = if (absMax > 1.0) absMax else 1.0
        motors.forEachIndexed { i, it -> it.setSpeed(wheels[i] / scalar) }
    }
}
